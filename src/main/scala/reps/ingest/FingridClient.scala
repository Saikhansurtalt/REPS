package reps.ingest

import reps.model.*
import java.net.{HttpURLConnection, URL}
import scala.io.Source
import scala.util.Try
import java.time.LocalDateTime

object FingridClient:

  final case class DatasetConfig(
                                  variableId: Int,
                                  source: EnergySource
                                )

  def fetch(
             config: DatasetConfig,
             startTime: String,
             endTime: String
           ): Either[String, List[EnergyRecord]] =

    Option(System.getenv("FINGRID_API_KEY"))
      .toRight("Missing FINGRID_API_KEY environment variable")
      .flatMap { apiKey =>

        val url =
          s"https://data.fingrid.fi/api/data" +
            s"?datasets=${config.variableId}" +
            s"&startTime=$startTime" +
            s"&endTime=$endTime" +
            s"&format=json"

        Try {
          val connection =
            new URL(url).openConnection().asInstanceOf[HttpURLConnection]

          connection.setRequestProperty("x-api-key", apiKey)
          connection.setRequestMethod("GET")
          connection.setConnectTimeout(10000)
          connection.setReadTimeout(10000)

          val json =
            Source.fromInputStream(connection.getInputStream).mkString

          connection.disconnect()

          parse(json, config.source)
        }.toEither.left.map(_.getMessage)
      }

  private def parse(
                     json: String,
                     source: EnergySource
                   ): List[EnergyRecord] =
    val pattern =
      """"startTime"\s*:\s*"([^"]+)".*?"value"\s*:\s*([0-9.]+)""".r

    pattern.findAllMatchIn(json).toList.map { m =>
      EnergyRecord(
        LocalDateTime.parse(m.group(1).replace(".000Z", "")),
        source,
        m.group(2).toDouble
      )
    }