package com.pinpoint

import play.api.libs.json.{ JsPath, Writes }
import spray.json.{ DefaultJsonProtocol, DeserializationException, JsValue, RootJsonFormat }
import play.api.libs.functional.syntax._

final case class Coord(lat: String, lon: String)

case class Container(uri: String, content: Seq[Coord])

object Container {
  implicit val CoordWrites: Writes[Coord] = (
    (JsPath \ "lat").write[String] and
    (JsPath \ "lon").write[String])(unlift(Coord.unapply))
  implicit val ContainerWrites: Writes[Container] = (
    (JsPath \ "type").write[String] and
    (JsPath \ "coords").write[Seq[Coord]])(unlift(Container.unapply))
}

case class UriList(items: Vector[String])

object UriList {

  import DefaultJsonProtocol._
  implicit val format: RootJsonFormat[UriList] = jsonFormat1(UriList.apply)

  implicit object ReadUrisFormat extends RootJsonFormat[UriList] {

    def read(value: JsValue): UriList = {
      value.asJsObject.getFields("uris") match {
        case Seq(uri) =>
          new UriList(uri.convertTo[Vector[String]])
        case _ => throw new DeserializationException("ClassInfo expected")
      }
    }

    override def write(obj: UriList): JsValue = ???
  }
}