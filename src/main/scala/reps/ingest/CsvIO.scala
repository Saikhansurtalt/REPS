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
 * This file provides CSV input and output functionality for the REPS system.
 * File operations are isolated here to limit side effects and keep the
 * functional core pure.
 */

package reps.ingest

import reps.model.*
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.*

/**
 * Handles reading and writing energy production data in CSV format.
 *
 * This object isolates file system side effects from the rest of the system.
 * All parsing errors and file related issues are handled explicitly.
 */
object CsvIO:

  /** CSV file header row. */
  private val Header = "timestamp,source,energyMWh"

  /**
   * Writes energy records to a CSV file.
   *
   * This function performs a side effect by writing data to disk.
   * Each energy record is converted into a single CSV row.
   *
   * @param path
   *   File path where the CSV file will be written.
   * @param records
   *   A list of energy production records to store.
   */
  def write(path: String, records: List[EnergyRecord]): Unit =
    val lines =
      Header ::
        records.map { r =>
          s"${r.timestamp},${r.source},${r.energyMWh}"
        }

    Files.write(Paths.get(path), lines.asJava)

  /**
   * Reads energy records from a CSV file.
   *
   * The function returns an Either to safely model failure cases
   * such as missing files or parsing errors.
   *
   * @param path
   *   File path of the CSV file.
   * @return
   *   Either an error message or a list of parsed energy records.
   */
  def read(path: String): Either[String, List[EnergyRecord]] =
    if !Files.exists(Paths.get(path)) then
      Left("CSV file not found")
    else
      Right(
        Files.readAllLines(Paths.get(path)).asScala.toList
          .drop(1) // remove header row
          .flatMap { line =>
            line.split(",").toList match
              case ts :: src :: value :: Nil =>
                EnergySource.fromString(src).map { source =>
                  EnergyRecord(
                    java.time.LocalDateTime.parse(ts),
                    source,
                    value.toDouble
                  )
                }
              case _ => None
          }
      )