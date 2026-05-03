
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
 * This file contains alert detection logic for the REPS system.
 * All alert generation is based on pure functional transformations
 * of immutable energy production data.
 */

package reps.core

import reps.model.*

/**
 * Provides functions for detecting abnormal conditions
 * in renewable energy production data.
 *
 * All functions in this object are pure and side effect free.
 * They analyze input data and return alerts as values.
 */
object Alerts:

  /**
   * Detects energy production values that fall below a given threshold.
   *
   * @param threshold
   *   The minimum acceptable energy production value in megawatt hours.
   * @param records
   *   A list of energy production records to analyze.
   * @return
   *   A list of LowOutput alerts for records below the threshold.
   */
  def detectLowOutput(threshold: Double)
                     (records: List[EnergyRecord]): List[Alert] =
    records
      .filter(_.energyMWh < threshold)
      .map(r => Alert.LowOutput(r.source, r.energyMWh))

  /**
   * Detects missing renewable energy sources in the dataset.
   *
   * This function compares the expected set of energy sources
   * with the sources actually present in the records.
   *
   * @param expected
   *   The set of energy sources that are expected to appear.
   * @param records
   *   A list of energy production records.
   * @return
   *   A list of Malfunction alerts for missing energy sources.
   */
  def detectMissingSources(
                            expected: Set[EnergySource],
                            records: List[EnergyRecord]
                          ): List[Alert] =
    val present = records.map(_.source).toSet
    (expected -- present)
      .toList
      .map(Alert.Malfunction.apply)