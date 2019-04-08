package com.tile

import java.io.File

import spray.json.DefaultJsonProtocol

import scala.sys.process.Process

object TileService extends DefaultJsonProtocol {
  def TileImage(imageName: String): String = {
    val file = getImageNameInDirectory(imageName)
    val doGdalPython = Process("gdal2tiles.py ".concat(file + " " + imageName))
    "Image tiled in /res/".concat(imageName)
  }

  def getImageNameInDirectory(imageName: String): Option[String] = {
    val d = new File("/res")
    val fileName: Option[String] = if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList.filter { file =>
        file.getName.startsWith(imageName)
      } match {
        case Nil => None
        case list => Some(list.head.getName)
      } // get first
    } else {
      None
    }
    fileName
  }

  def doTile(file: File, dirName: String): String = {
    //TO DO : lancer programme gdal2tiles.py file dirName
    "image tuilée et disponible dans /res/dirName"
  }
}
