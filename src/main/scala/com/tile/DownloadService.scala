package com.tile

import java.io.{ BufferedOutputStream, File, FileOutputStream }
import java.nio.file.{ Files, Path, Paths }
import java.util.Base64

import scala.util.{ Failure, Success, Try }
import com.shared.{ Job, JobQueue }
import scalaj.http.Http

import scala.sys.process.Process

object DownloadService {

  def downloadImages(queue: JobQueue): Unit = {
    queue.jobs.foreach(job => {

      val destination: Path = Paths.get(s"res/${job.uuid}/")
      val tiff: Path = Paths.get(destination.toAbsolutePath.toString, s"${job.uuid}.tiff")
      val zip: Path = Paths.get(destination.toAbsolutePath.toString, s"${job.uuid}.zip")

      queue.markJobAsDownloading(job.uuid)
      downloadAsZip(job, zip) match {
        case Failure(_) => queue.markJobAsFailed(job.uuid)
        case Success(_) => {
          queue.markJobAsConvertingToTiff(job.uuid)
          unzip(zip, destination)
            .flatMap(extractSubDataSet)
            .flatMap(convertToTiff(_)(tiff)) match {
              case Failure(_) => queue.markJobAsFailed(job.uuid)
              case Success(_) => queue.markJobAsCompleted(job.uuid)
            }
        }
      }
    })
  }

  def filter(file: File): Boolean = file.toString.endsWith(".xml") && !file.toString.endsWith("INSPIRE.xml")

  def extractMetadata(imageMetadataPath: Path): Try[String] = Try(Process(s"gdalinfo $imageMetadataPath").!!)

  def extractSubDataSet(imageFolder: Path): Try[String] = Try {
    val rootDirectory = imageFolder.toFile.listFiles.head.toPath
    val imageMetadataPath: Path = rootDirectory.toFile.listFiles(f => filter(f)).head.toPath
    val imageMetadata: String = extractMetadata(imageMetadataPath) match {
      case Failure(exception) => throw new Exception(exception)
      case Success(metadata) => metadata
    }
    val subDataSetRegexp = """[\s\S]+SUBDATASET_1_NAME=([\S]+)[\s\S]+""".r
    val subDataSetRegexp(subDataSetName) = imageMetadata
    subDataSetName
  }

  def convertToTiff(subDataSet: String)(tiff: Path): Try[String] = Try(Process(s"gdal_translate $subDataSet $tiff").!!)

  def downloadAsZip(job: Job, zip: Path): Try[Unit] = Try {
    val dir: Path = zip.getParent
    if (dir != null) Files.createDirectories(dir)

    val userCredentials = "sysm@hotmail.fr:Choucas64!"
    val basicAuth = "Basic " + new String(Base64.getEncoder.encode(userCredentials.getBytes))

    val response = Http(job.url)
      .timeout(connTimeoutMs = 15000, readTimeoutMs = 15000)
      .header("Authorization", basicAuth)
      .asBytes

    // If status is 202, cancel the operation as the resource is unavailable.
    if (response.code < 200 || response.code > 299 || response.code == 202)
      throw new Exception(s"Download failed for job ${job.uuid}")

    val is = new FileOutputStream(zip.toAbsolutePath.toString)
    val bs = new BufferedOutputStream(is)

    bs.write(response.body, 0, response.body.length)
    bs.close()
    is.close()
  }

  def unzip(zip: Path, destination: Path): Try[Path] = Try {
    Process(s"unzip $zip -d $destination").!!
    Files.deleteIfExists(zip)
    destination
  }

  def deleteRecursively(file: File): Unit = {
    if (file.isDirectory)
      file.listFiles.foreach(deleteRecursively)
    if (file.exists && !file.delete)
      throw new Exception(s"Unable to delete ${file.getAbsolutePath}")
  }

}

