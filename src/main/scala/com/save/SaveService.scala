package com.save

import spray.json.DefaultJsonProtocol

import scala.sys.process.Process

object SaveService extends DefaultJsonProtocol{
  def SaveImageWithHdfs(imageName: String): String = {
    val commands = "scp res/" + imageName + ".zip res/" + imageName + ".tiff Poppy@10.100.2.2:/home/Poppy"
    val firstCopy = Process(commands).!!
    val secondCommand = "ssh Poppy@10.100.2.2 hadoop fs -put /home/Poppy/" + imageName + ".zip /home/Poppy/" + imageName + ".tiff /user/poppy"
    val secondCopy = Process(secondCommand).!!
    "files uploaded"
  }
}
