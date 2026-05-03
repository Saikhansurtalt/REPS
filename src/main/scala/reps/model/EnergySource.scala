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
 * This file defines the supported renewable energy source types
 * used throughout the REPS system.
 */

package reps.model

/**
 * Represents the different renewable energy sources
 * supported by the REPS system.
 *
 * The enum ensures type safety when working with
 * energy source categories.
 */
enum EnergySource:
  case Solar
  case Wind
  case Hydro

/**
 * Companion object for EnergySource.
 *
 * Provides utility methods for converting external
 * string values into EnergySource instances.
 */
object EnergySource:

  /**
   * Converts a string value into an EnergySource.
   *
   * The comparison is case insensitive. Invalid or
   * unknown values return None.
   *
   * @param value
   *   String representation of an energy source.
   * @return
   *   Some(EnergySource) if conversion succeeds, otherwise None.
   */
  def fromString(value: String): Option[EnergySource] =
    value.toLowerCase match
      case "solar" => Some(EnergySource.Solar)
      case "wind"  => Some(EnergySource.Wind)
      case "hydro" => Some(EnergySource.Hydro)
      case _       => None