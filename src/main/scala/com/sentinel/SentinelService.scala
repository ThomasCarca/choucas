package com.sentinel

import java.text.SimpleDateFormat
import java.util.Date

import com.box.BoundingBox
import scalaj.http.Http
import play.api.libs.json.{JsArray, JsDefined, JsNumber, Json, JsValue => PlayValue}


object SentinelService {

  private val formatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

  def fetchImagesInfo(box: BoundingBox): Vector[ImageInfo] = {
    val response = Http(s"https://peps.cnes.fr/resto/api/collections/S2/search.json?box=${box.toString()}&startDate=2016-12-01&completionDate=2018-12-31").header("Accept", "application/json").asString
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
    val date: Date = formatter.parse((properties \ "startDate").get.as[String])
    ImageInfo(download, preview, cloud, date)
  }

}

