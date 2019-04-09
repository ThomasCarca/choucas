package com.download

import java.util.Date

import com.box.BoundingBox
import scalaj.http.Http
import play.api.libs.json.{JsArray, JsBoolean, JsDefined, JsNull, JsNumber, JsObject, JsString, JsUndefined, Json, JsValue => PlayValue}


object DownloadService {

  def fetchDownloadUrls(box: BoundingBox): Vector[ImageInfo] = {
    val response = Http(s"https://peps.cnes.fr/resto/api/collections/S2/search.json?box=${box.toString()}&startDate=2016-12-01&completionDate=2018-12-31").header("Accept", "application/json").asString
    val body : PlayValue = Json.parse(response.body)
    val jsFeatures = body.\("features")
    val imageInfos: Vector[ImageInfo] = jsFeatures match {
      case JsDefined(features) => features match {
        case JsArray(images) => images.map(image => {
          ImageInfo("", "", "", new Date())
        }).to[Vector]
        case _ => Vector.empty
      }
      case undefined: JsUndefined => Vector.empty
    }
    return imageInfos
  }


//  def fetchDownloadUrls(box: BoundingBox): Vector[String] = {
//    val response = Http(s"https://peps.cnes.fr/resto/api/collections/S2/search.json?box=${box.toString()}&startDate=2016-12-01&completionDate=2018-12-31").header("Accept", "application/json").asString
//    val body = response.body.parseJson
//    val jsFeatures = body.asJsObject.fields.get("features")
//    val length = jsFeatures match {
//      case Some(features) => features match {
//        case JsArray(elements) => elements.foreach(js => println(js))
//        case _ => "-1"
//      }
//      case None => "-1"
//    }
//
//    return Vector("")
//  }

}

