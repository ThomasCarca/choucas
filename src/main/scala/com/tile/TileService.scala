package com.tile

import java.io.File

import com.shared.{Job, JobQueues}
import spray.json.DefaultJsonProtocol

import scala.sys.process.Process
import scala.util.Try

object TileService extends DefaultJsonProtocol {

  def tileImage(job: Job): Try[Any] = {
    val queue = JobQueues.queues.get(job.uuid)
    val file = getImageNameInDirectory(job.uuid)
    file match {
      case "" => queue.fail(job.uuid)
      case _ => {
        val commands = "gdal2tiles.py res/" + file
        val doGdalPython = Process(commands).!!
        val moveToZip = Process(s"zip -r res/${job.uuid}.zip res/${job.uuid} res/${job.uuid}.tiff").!!
        SaveService.saveImageWithHdfs(job)
      }
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
