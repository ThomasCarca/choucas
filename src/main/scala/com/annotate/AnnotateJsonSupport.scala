package com.annotate

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class URIs(uris: Vector[String])

trait AnnotateJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val urisJsonFormat: RootJsonFormat[URIs] = jsonFormat1(URIs)
}