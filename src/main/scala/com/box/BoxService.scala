package com.box

import com.shared.{BoundingBox, Coordinates}

object BoxService {

  def createBoundingBox(coordinates: Vector[Coordinates]): BoundingBox = {
    coordinates.foldLeft(BoundingBox(Coordinates(90, 180), Coordinates(-90, -180)))(
      (box, coordinates) => {
        val swCoordinates = coordinates match {
          case Coordinates(lat, lon) if lat < box.swCoordinates.lat && lon < box.swCoordinates.lon => Coordinates(lat, lon)
          case Coordinates(lat, _) if lat < box.swCoordinates.lat => Coordinates(lat, box.swCoordinates.lon)
          case Coordinates(_, lon) if lon < box.swCoordinates.lon => Coordinates(box.swCoordinates.lat, lon)
          case _ => box.swCoordinates
        }
        val neCoordinates = coordinates match {
          case Coordinates(lat, lon) if lat > box.neCoordinates.lat && lon > box.neCoordinates.lon => Coordinates(lat, lon)
          case Coordinates(lat, _) if lat > box.neCoordinates.lat => Coordinates(lat, box.neCoordinates.lon)
          case Coordinates(_, lon) if lon > box.neCoordinates.lon => Coordinates(box.neCoordinates.lat, lon)
          case _ => box.neCoordinates
        }
        BoundingBox(swCoordinates, neCoordinates)
      }
    )
  }
}

