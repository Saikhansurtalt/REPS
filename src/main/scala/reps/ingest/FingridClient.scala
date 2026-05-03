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
 * This file handles communication with the Fingrid Open Data API.
 * Network interaction and JSON parsing are isolated here to limit
 * side effects and keep the functional core independent.
 */

package reps.ingest

import reps.model.*
import java.net.{HttpURLConnection, URL}
import scala.io.Source
import scala.util.Try
import java.time.LocalDateTime

/**
 * Provides functionality for retrieving renewable energy production
 * data from the Fingrid Open Data API.
 *
 * This object performs controlled side effects related to network
 * communication and returns results using Either for safe error handling.
 */
object FingridClient:

  /**
   * Configuration describing a Fingrid dataset.
   *
   * @param variableId
   *   Identifier of the Fingrid dataset to query.
   * @param source
   *   The energy source associated with the dataset.
   */
  final case class DatasetConfig(
                                  variableId: Int,
                                  source: EnergySource
                                )

  /**
   * Fetches energy production data from the Fingrid API.
   *
   * An API key is read from the FINGRID_API_KEY environment variable.
   * All error cases are modeled using Either instead of exceptions.
   *
   * @param config
   *   Dataset configuration including variable identifier and energy source.
   * @param startTime
   *   Start time of the query in ISO format.
   * @param endTime
   *   End time of the query in ISO format.
   * @return
   *   Either an error message or a list of parsed energy records.
   */
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

  /**
   * Parses JSON response content returned by the Fingrid API.
   *
   * A simple pattern matching approach is used to extract timestamps
   * and energy production values from the response.
   *
   * @param json
   *   Raw JSON response string.
   * @param source
   *   Energy source associated with the dataset.
   * @return
   *   A list of parsed energy production records.
   */
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