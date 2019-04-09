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