package com.shared

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._
  import com.shared.utils.DateMarshalling._

  implicit val urisJsonFormat: RootJsonFormat[URIs] = jsonFormat1(URIs)
  implicit val coordinateJsonFormat: RootJsonFormat[Coordinates] = jsonFormat2(Coordinates)
  implicit val boundingBoxJsonFormat: RootJsonFormat[BoundingBox] = jsonFormat2(BoundingBox)
  implicit val imageInfoJsonFormat: RootJsonFormat[ImageInfo] = jsonFormat4(ImageInfo)
}
