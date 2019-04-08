package com.box

object BoxService {

  def createBoundingBox(coordinates: Vector[Coordinate]): BoundingBox = {
    coordinates.foldLeft(BoundingBox(Coordinate(90, 180), Coordinate(-90, -180)))(
      (box, coordinate) => {
        val swCoordinate = coordinate match {
          case Coordinate(lat, lon) if lat < box.swCoordinate.lat && lon < box.swCoordinate.lon => Coordinate(lat, lon)
          case Coordinate(lat, _) if lat < box.swCoordinate.lat => Coordinate(lat, box.swCoordinate.lon)
          case Coordinate(_, lon) if lon < box.swCoordinate.lon => Coordinate(box.swCoordinate.lat, lon)
          case _ => box.swCoordinate
        }
        val neCoordinate = coordinate match {
          case Coordinate(lat, lon) if lat > box.neCoordinate.lat && lon > box.neCoordinate.lon => Coordinate(lat, lon)
          case Coordinate(lat, _) if lat > box.neCoordinate.lat => Coordinate(lat, box.neCoordinate.lon)
          case Coordinate(_, lon) if lon > box.neCoordinate.lon => Coordinate(box.neCoordinate.lat, lon)
          case _ => box.neCoordinate
        }
        BoundingBox(swCoordinate, neCoordinate)
      }
    )
  }
}

