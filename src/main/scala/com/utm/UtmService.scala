package com.utm

import play.api.libs.json.{ JsArray, Json, JsValue => PlayValue }
import scalaj.http.{ Http, HttpResponse }

object UtmService {

  val API_TOKEN: String = "bac58846ebbd490abce2b1cddb5903eb"

  def mean(s: Vector[Coordinate]) = {
    val coordinateSum = s.foldLeft(Coordinate(0, 0))( (acc, c) => Coordinate(acc.lat + c.lat, acc.lon + c.lon))
    Coordinate(coordinateSum.lat/s.size, coordinateSum.lon/s.size)
  }

  def fetchUTM(coordinates: Vector[Coordinate]): String = {
    val centeredCoordinate = mean(coordinates)
    val lat = centeredCoordinate.lat
    val lon = centeredCoordinate.lon
    val response = Http("https://api.opencagedata.com/geocode/v1/json").params(Map("q" -> s"$lat,$lon", "key" -> API_TOKEN)).asString
    filterUTM(response)
  }

  def filterUTM(response: HttpResponse[String]): String = {
    val body: PlayValue = Json.parse(response.body)
    val results = body \ "results"
    if (results.as[JsArray].value.size == 0) return ""
    val mgrs = (results \ 0 \ "annotations" \ "MGRS").as[String].substring(0, 5)
    return mgrs
  }

}
