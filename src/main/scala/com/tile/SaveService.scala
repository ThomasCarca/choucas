package com.tile


import com.shared.{Job, JobQueue}

import scala.sys.process.Process
import scala.util.{Failure, Success, Try}

object SaveService {
  def saveImageWithHdfs(queue: JobQueue): Unit = {
    queue.jobs.foreach(job => {
      queue.markJobAsSavingToThorusCloud(job.uuid)
      saveImageWithHdfs(job) match {
        case Failure(_) => queue.markJobAsFailed(job.uuid)
        case Success(_) => queue.markJobAsCompleted(job.uuid)
      }
    })
  }

  def saveImageWithHdfs(job: Job): Try[Any] = {
    val commands = s"scp res/${job.uuid}.zip Poppy@10.100.2.2:/home/Poppy"
    val secondCommand = s"ssh Poppy@10.100.2.2 hadoop fs -put /home/Poppy/${job.uuid}.zip /user/poppy"

    Try({
      val firstCopy = Process(commands).!!

      val secondCopy = Process(secondCommand).!!
    })

  }
}
