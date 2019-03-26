package com.hike

import scala.concurrent.Future

object HikeService {
  var hikes : Seq[Hike] = Nil

  def getHikes() : Future[Seq[Hike]] = Future{
    hikes
  }
}