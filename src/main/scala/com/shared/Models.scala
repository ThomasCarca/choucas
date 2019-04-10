package com.shared

import java.util.Date

final case class URIs(uris: Vector[String])
final case class ImageInfo(download: String, preview: String, cloud: Float, date: Date)
final case class Coordinates(lat: Float, lon: Float)
final case class BoundingBox(swCoordinates: Coordinates, neCoordinates: Coordinates) {
  override def toString(): String = {
    s"${swCoordinates.lat},${swCoordinates.lon},${neCoordinates.lat},${neCoordinates.lon}"
  }
}
case class MetaData(data: Vector[String])
final case class Tuile(download: String, name: String, metaData: MetaData)
final case class DataElastic(box: BoundingBox, tuiles: Vector[Tuile], image: ImageInfo, metaData: MetaData)