/*
 * Project: REPS – Renewable Energy Production System
 *
 * Course: Functional Programming
 *
 * Team Members:
 * Saikhansurtalt Sumiyabazar 002329527
 * Inthuja Inthumathan 002599632
 *
 * Description:
 * This file is part of the REPS project submission.
 */

package reps

import reps.model.*
import reps.core.*
import reps.ingest.*
import reps.view.*

import java.time.LocalDateTime

@main def main(): Unit =
  println("=== REPS SYSTEM START ===")

  // --------------------------------------------------
  // Configuration
  // --------------------------------------------------
  val useFingridApi = true   // set to false to force CSV fallback
  val csvPath = "energy-data.csv"

  val fingridConfig =
    FingridClient.DatasetConfig(
      variableId = 245,          // ✅ your confirmed dataset
      source = EnergySource.Wind // adjust if Fingrid labels it differently
    )

  // --------------------------------------------------
  // Load data (API → CSV fallback)
  // --------------------------------------------------
  val records =
    if useFingridApi then
      FingridClient.fetch(
        fingridConfig,
        startTime = "2024-04-12T00:00:00Z",
        endTime   = "2024-04-12T23:59:59Z"
      ) match
        case Right(data) =>
          println("Data fetched from Fingrid API")
          CsvIO.write(csvPath, data)
          data
        case Left(error) =>
          println(s"Fingrid API error: $error")
          println("Falling back to CSV")
          CsvIO.read(csvPath).getOrElse(Nil)
    else
      CsvIO.read(csvPath).getOrElse(Nil)

  // --------------------------------------------------
  // Display records
  // --------------------------------------------------
  ConsoleView.showRecords(records)

  // --------------------------------------------------
  // Statistics
  // --------------------------------------------------
  val values = records.map(_.energyMWh)

  println("\n--- Statistics ---")
  println("Mean: " + Statistics.mean(values))
  println("Median: " + Statistics.median(values))
  println("Mode: " + Statistics.mode(values))
  println("Range: " + Statistics.range(values))
  println("Midrange: " + Statistics.midrange(values))

  // --------------------------------------------------
  // Filtering example (current hour)
  // --------------------------------------------------
  val currentHour = LocalDateTime.now.getHour
  val filtered =
    Filters.byHour(currentHour)(records)

  println(s"\nRecords for hour $currentHour:")
  ConsoleView.showRecords(filtered)

  // --------------------------------------------------
  // Alerts
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