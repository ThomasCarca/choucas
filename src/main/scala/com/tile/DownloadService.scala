package com.tile

import java.io.{BufferedOutputStream, FileOutputStream}
import java.nio.file.{Files, Paths}
import java.util.Base64
import scala.util.{Try, Success, Failure}

import com.shared.{Job, JobQueue}
import scalaj.http.Http

object DownloadService {

  def downloadImages(queue: JobQueue): Unit  = {
    queue.jobs.foreach(job => {
      queue.start(job.uuid)
      downloadImage(job) match {
        case Success(_) => {
          TileService.tileImage(job)
          queue.complete(job.uuid)
        }
        case Failure(_) => {
          queue.fail(job.uuid)
        }
      }
    })
  }

  def downloadImage(job: Job): Try[Any] = {
    val path = s"res/images/${job.uuid}.zip"
    val dir = Paths.get(path).getParent
    if (dir != null) Files.createDirectories(dir)

    val userCredentials = "sysm@hotmail.fr:Choucas64!"
    val basicAuth = "Basic " + new String(Base64.getEncoder.encode(userCredentials.getBytes))

    Try({
      val response = Http(job.url)
        .timeout(connTimeoutMs = 15000, readTimeoutMs = 15000)
        .header("Authorization", basicAuth)
        .asBytes

      if (response.code < 200 || response.code > 299)
        throw new Exception

      val is = new FileOutputStream(path)
      val bs = new BufferedOutputStream(is)

      bs.write(response.body, 0, response.body.length)
      bs.close()
      is.close()
    })
  }


}

