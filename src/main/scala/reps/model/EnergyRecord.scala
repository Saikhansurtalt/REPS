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