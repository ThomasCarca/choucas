package com.annotate

import scalaj.http.{Http, HttpResponse}
import spray.json._

object AnnotateService {


  def fetchAnnotations(text: String): URIs = {
    val response = Http("http://icc.pau.eisti.fr/rest/annotate").header("Accept", "application/json").postForm(Seq("text" -> text)).asString
    filterURIs(response)
  }

  def filterURIs(response: HttpResponse[String]): URIs = {
    val body = response.body.parseJson.asJsObject
    val maybeResources = body.fields.get("Resources")
    maybeResources match {
      case Some(resources) => {
        resources match {
          case JsArray(elements) => {
            val textURIs = elements.map(e => e.asJsObject.fields.get("@URI")).flatten
            val uris = textURIs.map(text => text.toString().replace("\"", ""))
            return URIs(uris.distinct)
          }
          case _ => URIs(Vector.empty[String])
        }
      }
      case None => URIs(Vector.empty[String])
    }
  }


}
