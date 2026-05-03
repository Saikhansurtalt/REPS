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
 * This file contains filtering functions for energy production data
 * used in the REPS system. All filters are implemented as pure
 * functions operating on immutable collections.
 */

package reps.core

import reps.model.*
import java.time.LocalDate

/**
 * Provides functions for filtering energy production records
 * based on time related criteria.
 *
 * All functions in this object are pure and do not mutate input data.
 */
object Filters:

  /**
   * Filters energy records by a specific hour of the day.
   *
   * @param hour
   *   The hour to filter by, in 24 hour format.
   * @param records
   *   A list of energy production records.
   * @return
   *   A list of records that match the given hour.
   */
  def byHour(hour: Int)
            (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.hour == hour)

  /**
   * Filters energy records by a specific calendar day.
   *
   * @param date
   *   The date to filter by.
   * @param records
   *   A list of energy production records.
   * @return
   *   A list of records that match the given date.
   */
  def byDay(date: LocalDate)
           (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.date == date)

  /**
   * Filters energy records by a specific month.
   *
   * @param month
   *   The month number to filter by, where January is 1 and December is 12.
   * @param records
   *   A list of energy production records.
   * @return
   *   A list of records that belong to the given month.
   */
  def byMonth(month: Int)
             (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.date.getMonthValue == month)