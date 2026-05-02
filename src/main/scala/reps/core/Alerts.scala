package reps.core

import reps.model.*

object Alerts:

  def detectLowOutput(threshold: Double)
                     (records: List[EnergyRecord]): List[Alert] =
    records
      .filter(_.energyMWh < threshold)
      .map(r => Alert.LowOutput(r.source, r.energyMWh))

  def detectMissingSources(
                            expected: Set[EnergySource],
                            records: List[EnergyRecord]
                          ): List[Alert] =
    val present = records.map(_.source).toSet
    (expected -- present)
      .toList
      .map(Alert.Malfunction)