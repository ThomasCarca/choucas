package com.pinpoint

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class Coord(coords: Vector[String])

trait AnnotateJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val productsJsonFormat: RootJsonFormat[Coord] = jsonFormat1(Coord)
}