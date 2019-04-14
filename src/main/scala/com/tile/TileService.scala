package com.tile

import java.io.File

import com.shared.{Job, JobQueue}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.sys.process.Process
import scala.util.{Failure, Success, Try}

object TileService extends DefaultJsonProtocol {

  def tileImage(queue: JobQueue): Unit = {
    queue.jobs.foreach(job => {
      queue.markJobAsTiling(job.uuid)
      val file = getImageNameInDirectory(job.uuid)
      tileAndZip(job, file) match {
        case Failure(_) => queue.markJobAsFailed(job.uuid)
        case Success(_) => queue.markJobAsCompleted(job.uuid)
      }
    })
  }

  def tileAndZip(job: Job, file: String): Try[Any] = {
    Try({
      val commands = "gdal2tiles.py res/" + file
      val doGdalPython = Process(commands).!!
      val moveToZip = Process(s"zip -r res/${job.uuid}.zip res/${job.uuid} res/${job.uuid}.tiff").!!
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
