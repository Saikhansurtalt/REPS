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

package reps.ingest

import reps.model.*
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.*

object CsvIO:

  private val Header = "timestamp,source,energyMWh"

  /** Writes records to a CSV file.
   * Side effect: writes to disk.
   */
  def write(path: String, records: List[EnergyRecord]): Unit =
    val lines =
      Header ::
        records.map { r =>
          s"${r.timestamp},${r.source},${r.energyMWh}"
        }

    Files.write(Paths.get(path), lines.asJava)

  /** Reads records from a CSV file.
   * Returns Either for safe error handling.
   */
  def read(path: String): Either[String, List[EnergyRecord]] =
    if !Files.exists(Paths.get(path)) then
      Left("CSV file not found")
    else
      Right(
        Files.readAllLines(Paths.get(path)).asScala.toList
          .drop(1) // drop header
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
