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