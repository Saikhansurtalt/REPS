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

enum EnergySource:
  case Solar
  case Wind
  case Hydro

object EnergySource:
  def fromString(value: String): Option[EnergySource] =
    value.toLowerCase match
      case "solar" => Some(EnergySource.Solar)
      case "wind"  => Some(EnergySource.Wind)
      case "hydro" => Some(EnergySource.Hydro)
      case _       => None