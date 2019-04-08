package com.utm

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class Coordinate(lat: Float, lon: Float)

final case class Coordinates(coordinates: Vector[Coordinate])

trait UtmJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val coordinateJsonFormat: RootJsonFormat[Coordinate] = jsonFormat2(Coordinate)
  implicit val coordinatesJsonFormat: RootJsonFormat[Coordinates] = jsonFormat1(Coordinates)
}