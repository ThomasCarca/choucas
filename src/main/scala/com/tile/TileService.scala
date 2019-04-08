package com.tile

import spray.json.DefaultJsonProtocol

import scala.reflect.io.{Directory, File}

object TileService extends DefaultJsonProtocol {
  def TileImage(imageName: String): String = "stop"

  def findImageInDirectory(imageName: String): List[File] = {
    val d = new Directory("/res")
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def doTile(file: File, dirName: String): String = {
    //TO DO : lancer programme gdal2tiles.py file dirName
    "image tuilée et disponible dans /res/dirName"
  }
}
