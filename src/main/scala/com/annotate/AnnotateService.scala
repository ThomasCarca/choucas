package com.annotate

import scalaj.http.{Http, HttpResponse}
import spray.json._
import DefaultJsonProtocol._

object AnnotateService {


  def fetchAnnotations(text: String): String = {
    val response = Http("http://icc.pau.eisti.fr/rest/annotate").header("Accept", "application/json").postForm(Seq("text" -> text)).asString
    val uris = filterUris(response)
    uris match {
      case Some(_) => uris.toJson.prettyPrint
      case None => Vector.empty[JsValue].toJson.prettyPrint
    }
  }

  def filterUris(response: HttpResponse[String]): Option[Vector[JsValue]] = {
    val body = response.body.parseJson.asJsObject
    val maybeResources = body.fields.get("Resources")
    maybeResources match {
      case Some(resources) => {
        resources match {
          case JsArray(elements) => Some(elements.map(e => e.asJsObject.fields.get("@surfaceForm")).flatten)
          case _ => None
        }
      }
      case None => None
    }
  }


}
