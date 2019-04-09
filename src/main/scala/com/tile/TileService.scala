package com.tile

import java.io.File

import spray.json.DefaultJsonProtocol

import scala.sys.process.Process

object TileService extends DefaultJsonProtocol {
  def TileImage(imageName: String): String = {
    val file = getImageNameInDirectory(imageName)
    file match {
      case "" => "Image doesn't exists"
      case _ => {
        val commands = "gdal2tiles.py res/" + file + " res/" + imageName
        val doGdalPython = Process(commands).!!
        "Image tiled in /res/".concat(imageName)}
    }

  }

  def getImageNameInDirectory(imageName: String): String = {
    val d = new File("res")
    val fileName: String = if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList.filter { file =>
        file.getName.startsWith(imageName)
      } match {
        case Nil => ""
        case list => list.head.getName
      } // get first
    } else {
      ""
    }
    fileName
  }
}
