package com.annotate

import scalaj.http.{Http, HttpResponse}
import spray.json._
import DefaultJsonProtocol._

object AnnotateService {


  def fetchAnnotations(text: String): String = {
    val response = Http("http://icc.pau.eisti.fr/rest/annotate").header("Accept", "application/json").postForm(Seq("text" -> text)).asString
    filterUris(response).toJson.prettyPrint
  }

  def filterUris(response: HttpResponse[String]): Vector[JsValue] = {
    val body = response.body.parseJson.asJsObject
    val maybeResources = body.fields.get("Resources")
    maybeResources match {
      case Some(resources) => {
        resources match {
          case JsArray(elements) => elements.map(e => e.asJsObject.fields.get("@URI")).flatten
          case _ => Vector.empty[JsValue]
        }
      }
      case None => Vector.empty[JsValue]
    }
  }


}
