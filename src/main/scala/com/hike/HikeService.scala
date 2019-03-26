package com.hike

import scalaj.http._
import spray.json._
import DefaultJsonProtocol._

object HikeService extends DefaultJsonProtocol {
  var hikes : Seq[Hike] = Nil

  def getHikes() : Unit = {
    val hike = Http("https://choucas.blqn.fr/data/outing/")
      .header("Accept", "application/json")
      .asString
    val json = hike.body.parseJson
    val jsonObj = json match {
      case JsArray(elements) => elements.map(e => e.asJsObject.fields.get("description")).flatten
      case _ => None
    }
    val test = jsonObj
  }

}