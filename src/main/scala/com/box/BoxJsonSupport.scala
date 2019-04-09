package com.box

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class Coordinate(lat: Float, lon: Float)

final case class BoundingBox(swCoordinate: Coordinate, neCoordinate: Coordinate) {

  override def toString(): String = {
    s"${swCoordinate.lat},${swCoordinate.lon},${neCoordinate.lat},${neCoordinate.lon}"
  }
}

trait BoxJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val coordinateJsonFormat: RootJsonFormat[Coordinate] = jsonFormat2(Coordinate)
  implicit val boundingBoxJsonFormat: RootJsonFormat[BoundingBox] = jsonFormat2(BoundingBox)
}