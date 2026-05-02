package reps.core

import reps.model.*
import java.time.LocalDate

object Filters:

  def byHour(hour: Int)
            (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.hour == hour)

  def byDay(date: LocalDate)
           (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.date == date)

  def byMonth(month: Int)
             (records: List[EnergyRecord]): List[EnergyRecord] =
    records.filter(_.date.getMonthValue == month)