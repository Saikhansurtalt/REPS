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

package reps.model

import java.time.{LocalDate, LocalDateTime}

final case class EnergyRecord(
                               timestamp: LocalDateTime,
                               source: EnergySource,
                               energyMWh: Double
                             ):
  def date: LocalDate =
    timestamp.toLocalDate

  def hour: Int =
    timestamp.getHour