package com.annotate

import scalaj.http.Http
import spray.json._
import DefaultJsonProtocol._

object AnnotateService {
  def fetchAnnotation(text: String): String = {
    val annotation = Http("http://icc.pau.eisti.fr/rest/annotate").header("Accept", "application/json").postForm(Seq("text" -> text)).asString
    val response = annotation.body.parseJson.asJsObject
    val maybeResources = response.fields.get("Resources")
    val uris = maybeResources match {
      case Some(resources) => {
        resources match {
          case JsArray(elements) => elements.map(j => j.asJsObject.fields.get("@URI")).flatten
          case _ => None
        }
      }
      case None => None
    }
    return "Hello annotate ! "
  }
}
