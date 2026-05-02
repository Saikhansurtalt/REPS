package reps

import reps.model.*
import reps.core.*
import reps.ingest.*
import reps.view.*

import java.time.LocalDateTime

@main def main(): Unit =

  println("=== REPS SYSTEM START ===")

  // --------------------------------------------------
  // 1. Create sample energy data (offline demo data)
  // --------------------------------------------------
  val sampleRecords = List(
    EnergyRecord(
      LocalDateTime.now.minusHours(3),
      EnergySource.Solar,
      40.0
    ),
    EnergyRecord(
      LocalDateTime.now.minusHours(2),
      EnergySource.Wind,
      120.0
    ),
    EnergyRecord(
      LocalDateTime.now.minusHours(1),
      EnergySource.Hydro,
      300.0
    ),
    EnergyRecord(
      LocalDateTime.now,
      EnergySource.Solar,
      20.0
    )
  )

  // --------------------------------------------------
  // 2. Save records to CSV
  // --------------------------------------------------
  val csvPath = "energy-data.csv"
  CsvIO.write(csvPath, sampleRecords)
  println(s"Data written to $csvPath")

  // --------------------------------------------------
  // 3. Load records from CSV
  // --------------------------------------------------
  val records =
    CsvIO.read(csvPath) match
      case Right(data) =>
        println("Data successfully loaded from CSV")
        data
      case Left(error) =>
        println(s"Error loading CSV: $error")
        Nil

  // --------------------------------------------------
  // 4. Display loaded records
  // --------------------------------------------------
  ConsoleView.showRecords(records)

  // --------------------------------------------------
  // 5. Run statistics
  // --------------------------------------------------
  val energyValues = records.map(_.energyMWh)

  println("\n--- Statistics ---")
  println("Mean: " + Statistics.mean(energyValues))
  println("Median: " + Statistics.median(energyValues))
  println("Mode: " + Statistics.mode(energyValues))
  println("Range: " + Statistics.range(energyValues))
  println("Midrange: " + Statistics.midrange(energyValues))

  // --------------------------------------------------
  // 6. Run filtering example
  // --------------------------------------------------
  val currentHour = LocalDateTime.now.getHour
  val recordsThisHour =
    Filters.byHour(currentHour)(records)

  println(s"\nRecords for hour $currentHour:")
  ConsoleView.showRecords(recordsThisHour)

  // --------------------------------------------------
  // 7. Detect alerts
  // --------------------------------------------------
  val alerts =
    Alerts.detectLowOutput(50.0)(records) ++
      Alerts.detectMissingSources(
        Set(
          EnergySource.Solar,
          EnergySource.Wind,
          EnergySource.Hydro
        ),
        records
      )

  println("\n--- Alerts ---")
  ConsoleView.showAlerts(alerts)

  println("\n=== REPS SYSTEM END ===")