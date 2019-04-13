package com.shared

import java.util.Date
import java.util.UUID.randomUUID

final case class URIs(uris: Vector[String])

final case class ImageInfo(download: String, preview: String, cloud: Float, date: Date)

final case class Coordinates(lat: Float, lon: Float)

final case class BoundingBox(swCoordinates: Coordinates, neCoordinates: Coordinates) {
  override def toString: String = {
    s"${swCoordinates.lat},${swCoordinates.lon},${neCoordinates.lat},${neCoordinates.lon}"
  }
}

final case class DatedBoundingBox(swCoordinates: Coordinates, neCoordinates: Coordinates, startDate: Date, completionDate: Date) {
  override def toString: String = {
    s"${swCoordinates.lat},${swCoordinates.lon},${neCoordinates.lat},${neCoordinates.lon}"
  }
}

final case class Job(uuid: String, url: String, status: String = "PENDING")

final case class StaticJobQueue(uuid: String, progress: String, jobs: Vector[Job])

final case class JobQueue() {
  val uuid: String = randomUUID().toString
  var done: Int = 0
  var total: Int = 0
  var jobs: Vector[Job] = Vector.empty[Job]

  private def markJobAs(uuid: String)(status: String): Unit = {
    this.jobs = this.jobs.map(job => if (job.uuid == uuid) Job(job.uuid, job.url, status) else job)
  }

  def markJobAsDownloading(uuid: String): Unit = {
    markJobAs(uuid)("DOWNLOADING")
  }

  def markJobAsConvertingToTiff(uuid: String): Unit = {
    markJobAs(uuid)("CONVERTING TO TIFF")
  }

  def markJobAsTiling(uuid: String): Unit = {
    markJobAs(uuid)("TILING")
  }

  def markJobAsSavingToThorusCloud(uui: String): Unit = {
    markJobAs(uuid)("SAVING TO THORUS CLOUD")
  }

  def markJobAsCompleted(uuid: String): Unit = {
    markJobAs(uuid)("COMPLETE")
    this.done += 1
  }

  def markJobAsFailed(uuid: String): Unit = {
    markJobAs(uuid)("FAILURE")
    this.done += 1
  }

  def asStaticJobQueue(): StaticJobQueue = {
    StaticJobQueue(uuid, s"$done/$total", jobs)
  }
}

final case class JobQueueLocation(location: String)
final case class MetaData(data: Vector[String])
final case class Tuile(download: String, name: String, metaData: MetaData)
final case class DataElastic(box: BoundingBox, tuiles: Vector[Tuile], image: ImageInfo, metaData: MetaData)
