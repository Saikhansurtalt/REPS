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
 * This file provides statistical analysis functions used in the REPS system.
 * All calculations are implemented using pure functions and return optional
 * values to safely handle empty input data.
 */

package reps.core

/**
 * Contains statistical utility functions for analyzing numeric data.
 *
 * All functions in this object are pure, side effect free, and operate
 * on immutable collections. Empty input lists are handled safely by
 * returning None.
 */
object Statistics:

  /**
   * Calculates the mean (average) of a list of values.
   *
   * @param values
   *   A list of numeric values.
   * @return
   *   Some(mean) if the list is non empty, otherwise None.
   */
  def mean(values: List[Double]): Option[Double] =
    if values.isEmpty then None
    else Some(values.sum / values.size)

  /**
   * Calculates the median of a list of values.
   *
   * The list is first sorted. If the number of elements is odd,
   * the middle value is returned. If even, the average of the two
   * middle values is returned.
   *
   * @param values
   *   A list of numeric values.
   * @return
   *   Some(median) if the list is non empty, otherwise None.
   */
  def median(values: List[Double]): Option[Double] =
    val sorted = values.sorted
    sorted.size match
      case 0 => None
      case n if n % 2 == 1 =>
        Some(sorted(n / 2))
      case n =>
        Some((sorted(n / 2 - 1) + sorted(n / 2)) / 2)

  /**
   * Calculates the mode of a list of values.
   *
   * The mode is the value that appears most frequently.
   *
   * @param values
   *   A list of numeric values.
   * @return
   *   Some(mode) if the list is non empty, otherwise None.
   */
  def mode(values: List[Double]): Option[Double] =
    values.groupBy(identity)
      .maxByOption(_._2.size)
      .map(_._1)

  /**
   * Calculates the range of a list of values.
   *
   * The range is defined as the difference between the maximum
   * and minimum values.
   *
   * @param values
   *   A list of numeric values.
   * @return
   *   Some(range) if the list is non empty, otherwise None.
   */
  def range(values: List[Double]): Option[Double] =
    for
      min <- values.minOption
      max <- values.maxOption
    yield max - min

  /**
   * Calculates the midrange of a list of values.
   *
   * The midrange is defined as the average of the minimum
   * and maximum values.
   *
   * @param values
   *   A list of numeric values.
   * @return
   *   Some(midrange) if the list is non empty, otherwise None.
   */
  def midrange(values: List[Double]): Option[Double] =
    for
      min <- values.minOption
      max <- values.maxOption
    yield (min + max) / 2