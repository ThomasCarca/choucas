package com.sentinel

import java.util.Date

import spray.json.RootJsonFormat
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class ImageInfo(download: String, preview: String, cloud: Float, date: Date)

trait SentinelJsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._
  import com.shared.utils.DateMarshalling._

  implicit val imageInfoJsonFormat: RootJsonFormat[ImageInfo] = jsonFormat4(ImageInfo)
}