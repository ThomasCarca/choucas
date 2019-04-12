package com.tile

import java.io.File

import com.shared.{JobQueue}
import spray.json.DefaultJsonProtocol

import scala.sys.process.Process

object TileService extends DefaultJsonProtocol {

  def tileImage(queue: JobQueue): Unit = {
    queue.jobs.foreach(job => {
      queue.markJobAsTiling(job.uuid)
      val file = getImageNameInDirectory(job.uuid)
      file match {
        case "" => queue.markJobAsFailed(job.uuid)
        case _ => {
          val commands = "gdal2tiles.py res/" + file
          val doGdalPython = Process(commands).!!
          val moveToZip = Process(s"zip -r res/${job.uuid}.zip res/${job.uuid} res/${job.uuid}.tiff").!!
          queue.markJobAsCompleted(job.uuid)
          SaveService.saveImageWithHdfs(job)
        }
      }
    })
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
