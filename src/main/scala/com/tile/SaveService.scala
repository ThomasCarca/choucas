package com.tile


import com.shared.{Job, JobQueue}

import scala.sys.process.Process
import scala.util.Try

object SaveService {
//  def saveImageWithHdfs(queue: JobQueue): Unit = {
//    queue.jobs
//  }

  def saveImageWithHdfs(job: Job): Try[Any] = {
    val commands = s"scp res/${job.uuid}.zip Poppy@10.100.2.2:/home/Poppy"
    val secondCommand = s"ssh Poppy@10.100.2.2 hadoop fs -put /home/Poppy/${job.uuid}.zip /user/poppy"

    Try({
      val firstCopy = Process(commands).!!

      val secondCopy = Process(secondCommand).!!
    })

  }
}
