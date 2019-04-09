package com.shared

import java.util.Date

final case class URIs(uris: Vector[String])
final case class ImageInfo(download: String, preview: String, cloud: Float, date: Date)
final case class Coordinates(lat: Float, lon: Float)
final case class BoundingBox(swCoordinate: Coordinates, neCoordinate: Coordinates) {
  override def toString(): String = {
    s"${swCoordinate.lat},${swCoordinate.lon},${neCoordinate.lat},${neCoordinate.lon}"
  }
}