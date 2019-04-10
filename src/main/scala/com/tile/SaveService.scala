package com.tile

import spray.json.DefaultJsonProtocol

import scala.sys.process.Process

object SaveService extends DefaultJsonProtocol{
  def SaveImageWithHdfs(imageName: String): String = {
    val commands = "scp res/" + imageName + ".zip Poppy@10.100.2.2:/home/Poppy"
    val firstCopy = Process(commands).!!
    val secondCommand = "ssh Poppy@10.100.2.2 hadoop fs -put /home/Poppy/" + imageName + ".zip /user/poppy"
    val secondCopy = Process(secondCommand).!!
    "files uploaded"
  }
}
