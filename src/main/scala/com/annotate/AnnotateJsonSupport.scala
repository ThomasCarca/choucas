package com.annotate

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait AnnotateJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val productsJsonFormat: RootJsonFormat[URIs] = jsonFormat1(URIs)
}