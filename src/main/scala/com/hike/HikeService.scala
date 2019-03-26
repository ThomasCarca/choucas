package com.hike

import scalaj.http._

import scala.concurrent.Future

object HikeService {
  var hikes : Seq[Hike] = Nil

  def getHikes() : HttpResponse[String] = Http("https://choucas.blqn.fr/data/outing/")
    .header("Accept", "application/json").asString

}