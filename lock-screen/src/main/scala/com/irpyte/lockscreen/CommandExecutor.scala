package com.irpyte.lockscreen

import java.io.{BufferedReader, InputStreamReader}
import java.util.Objects

import com.typesafe.scalalogging.LazyLogging

object CommandExecutor extends LazyLogging{

  def execute(commands: Seq[String]): Unit = {
    val commandStr = commands.mkString(" ")

    logger.info(s"Executing $commandStr")
    val builder = new ProcessBuilder(commands: _*)
    builder.redirectErrorStream(true)
    val process = builder.start()

    val reader = new BufferedReader(new InputStreamReader(process.getInputStream, "UTF-8"))

    try {
      val processOutput = new StringBuilder

      var isFinished = false
      while (!isFinished) {
        val str = reader.readLine()
        if (Objects.nonNull(str)) {
          processOutput.append(str).append("\n")
        } else {
          isFinished = true
        }
      }
      process.waitFor()

      logger.info(s"\n${processOutput.toString()}")
      if (process.exitValue() != 0) {
        throw new IllegalStateException(s"Not successfull response code: ${process.exitValue()}")
      }

    } finally {
      reader.close()
    }
  }

}
