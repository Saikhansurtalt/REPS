package reps.core

object Statistics:

  def mean(values: List[Double]): Option[Double] =
    if values.isEmpty then None
    else Some(values.sum / values.size)

  def median(values: List[Double]): Option[Double] =
    val sorted = values.sorted
    sorted.size match
      case 0 => None
      case n if n % 2 == 1 =>
        Some(sorted(n / 2))
      case n =>
        Some((sorted(n / 2 - 1) + sorted(n / 2)) / 2)

  def mode(values: List[Double]): Option[Double] =
    values.groupBy(identity)
      .maxByOption(_._2.size)
      .map(_._1)

  def range(values: List[Double]): Option[Double] =
    for
      min <- values.minOption
      max <- values.maxOption
    yield max - min

  def midrange(values: List[Double]): Option[Double] =
    for
      min <- values.minOption
      max <- values.maxOption
    yield (min + max) / 2