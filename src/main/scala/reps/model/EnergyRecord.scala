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
 * This file defines the core data model representing a single
 * renewable energy production measurement used throughout the REPS system.
 */

package reps.model

import java.time.{LocalDate, LocalDateTime}

/**
 * Represents a single energy production record.
 *
 * Each record stores the timestamp of the measurement,
 * the energy source, and the produced energy amount
 * measured in megawatt hours.
 *
 * This class is immutable by design.
 */
final case class EnergyRecord(
                               timestamp: LocalDateTime,
                               source: EnergySource,
                               energyMWh: Double
                             ):

  /**
   * Extracts the calendar date from the timestamp.
   *
   * @return
   *   The date component of the timestamp.
   */
  def date: LocalDate =
    timestamp.toLocalDate

  /**
   * Extracts the hour of the day from the timestamp.
   *
   * @return
   *   The hour value in 24 hour format.
   */
  def hour: Int =
    timestamp.getHour