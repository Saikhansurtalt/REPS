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
 * This file contains the main entry point of the REPS application.
 * It coordinates data ingestion, processing, and presentation by
 * connecting the different system components.
 */

package reps

import reps.model.*
import reps.core.*
import reps.ingest.*
import reps.view.*

import java.time.LocalDateTime

/**
 * Entry point of the REPS application.
 *
 * This function acts as the orchestration layer that connects
 * side effecting operations with the pure functional core.
 * It contains no complex business logic itself.
 */
@main def main(): Unit =
  println("=== REPS SYSTEM START ===")

  // --------------------------------------------------
  // Configuration
  // --------------------------------------------------

  /**
   * Flag controlling whether the Fingrid API is used.
   * If set to false, the system will load data from CSV only.
   */
  val useFingridApi = true

  /** File path used for CSV storage and fallback. */
  val csvPath = "energy-data.csv"

  /**
   * Configuration for the Fingrid dataset being queried.
   * The variable identifier and energy source are defined here.
   */
  val fingridConfig =
    FingridClient.DatasetConfig(
      variableId = 245,
      source = EnergySource.Wind
    )

  // --------------------------------------------------
  // Load data (API first, CSV as fallback)
  // --------------------------------------------------

  /**
   * Loads energy production records.
   *
   * Data is fetched from the Fingrid API when enabled.
   * If API access fails, the system falls back to local CSV data.
   */
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
  // Display loaded records
  // --------------------------------------------------

  /** Displays all loaded energy records to the console. */
  ConsoleView.showRecords(records)

  // --------------------------------------------------
  // Statistical analysis
  // --------------------------------------------------

  /** Extract numeric energy values for statistical analysis. */
  val values = records.map(_.energyMWh)

  println("\n--- Statistics ---")
  println("Mean: " + Statistics.mean(values))
  println("Median: " + Statistics.median(values))
  println("Mode: " + Statistics.mode(values))
  println("Range: " + Statistics.range(values))
  println("Midrange: " + Statistics.midrange(values))

  // --------------------------------------------------
  // Filtering example
  // --------------------------------------------------

  /**
   * Demonstrates filtering by the current hour.
   */
  val currentHour = LocalDateTime.now.getHour
  val filtered =
    Filters.byHour(currentHour)(records)

  println(s"\nRecords for hour $currentHour:")
  ConsoleView.showRecords(filtered)

  // --------------------------------------------------
  // Alert detection
  // --------------------------------------------------

  /**
   * Generates alerts for low energy output and missing sources.
   */
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