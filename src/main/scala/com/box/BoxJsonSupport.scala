package com.box

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.shared.{BoundingBox, Coordinate}
import spray.json.DefaultJsonProtocol

trait BoxJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val coordinateJsonFormat: RootJsonFormat[Coordinate] = jsonFormat2(Coordinate)
  implicit val boundingBoxJsonFormat: RootJsonFormat[BoundingBox] = jsonFormat2(BoundingBox)
}