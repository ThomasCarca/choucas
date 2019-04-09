package com.box

import com.shared.{BoundingBox, Coordinates}

object BoxService {

  def createBoundingBox(coordinates: Vector[Coordinates]): BoundingBox = {
    coordinates.foldLeft(BoundingBox(Coordinates(90, 180), Coordinates(-90, -180)))(
      (box, coordinate) => {
        val swCoordinate = coordinate match {
          case Coordinates(lat, lon) if lat < box.swCoordinate.lat && lon < box.swCoordinate.lon => Coordinates(lat, lon)
          case Coordinates(lat, _) if lat < box.swCoordinate.lat => Coordinates(lat, box.swCoordinate.lon)
          case Coordinates(_, lon) if lon < box.swCoordinate.lon => Coordinates(box.swCoordinate.lat, lon)
          case _ => box.swCoordinate
        }
        val neCoordinate = coordinate match {
          case Coordinates(lat, lon) if lat > box.neCoordinate.lat && lon > box.neCoordinate.lon => Coordinates(lat, lon)
          case Coordinates(lat, _) if lat > box.neCoordinate.lat => Coordinates(lat, box.neCoordinate.lon)
          case Coordinates(_, lon) if lon > box.neCoordinate.lon => Coordinates(box.neCoordinate.lat, lon)
          case _ => box.neCoordinate
        }
        BoundingBox(swCoordinate, neCoordinate)
      }
    )
  }
}

