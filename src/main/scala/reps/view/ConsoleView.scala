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

package reps.view

import reps.model.*

object ConsoleView:

  /** Displays energy records to the console */
  def showRecords(records: List[EnergyRecord]): Unit =
    if records.isEmpty then
      println("No energy records to display.")
    else
      records.foreach { r =>
        println(
          s"${r.timestamp} | ${r.source} | ${r.energyMWh} MWh"
        )
      }

  /** Displays alerts to the console */
  def showAlerts(alerts: List[Alert]): Unit =
    if alerts.isEmpty then
      println("No alerts detected.")
    else
      alerts.foreach { alert =>
        println(s"ALERT: ${alert.message}")
      }