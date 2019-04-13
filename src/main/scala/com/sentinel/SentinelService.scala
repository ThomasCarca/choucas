package com.sentinel

import java.text.SimpleDateFormat
import java.util.Date

import com.shared.{DatedBoundingBox, ImageInfo}
import scalaj.http.Http
import play.api.libs.json.{JsArray, JsDefined, JsNumber, Json, JsValue => PlayValue}


object SentinelService {

  private val defaultFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private val simpleFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def fetchImagesInfo(box: DatedBoundingBox): Vector[ImageInfo] = {
    val startDate = simpleFormatter.format(box.startDate)
    val completionDate = simpleFormatter.format(box.completionDate)
    val response = Http(s"https://peps.cnes.fr/resto/api/collections/S2/search.json?maxRecords=9999&box=${box.toString}&startDate=$startDate&completionDate=$completionDate").header("Accept", "application/json").asString
    val body : PlayValue = Json.parse(response.body)
    extractImagesInfo(body)
  }

  def extractImagesInfo(body: PlayValue): Vector[ImageInfo] = {
    val jsFeatures = body.\("features")
    jsFeatures match {
      case JsDefined(features) => features match {
        case JsArray(images) => images.map(image => toImageInfo(image)).to[Vector]
        case _ => Vector.empty
      }
      case _ => Vector.empty
    }
  }

  def toImageInfo(image: PlayValue): ImageInfo = {
    val properties = image \ "properties"
    val download: String = (properties \ "services" \ "download" \ "url").get.as[String]
    val preview: String = (properties \ "quicklook").get.as[String]
    val cloud: Float = (properties \ "cloudCover").get match {
      case JsNumber(value) => value.toFloat
      case _ => Float.NaN
    }
    val date: Date = defaultFormatter.parse((properties \ "startDate").get.as[String])
    ImageInfo(download, preview, cloud, date)
  }

}

