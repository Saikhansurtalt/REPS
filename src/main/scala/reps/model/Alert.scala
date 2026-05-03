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

sealed trait Alert:
  def message: String

object Alert:

  final case class LowOutput(
                              source: EnergySource,
                              value: Double
                            ) extends Alert:
    val message =
      s"Low energy output detected for $source: $value MWh"

  final case class Malfunction(
                                source: EnergySource
                              ) extends Alert:
    val message =
      s"Possible malfunction detected for $source"