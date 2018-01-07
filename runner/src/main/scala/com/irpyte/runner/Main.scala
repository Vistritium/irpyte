package com.irpyte.runner

import java.nio.file.{Files, Paths}

import com.irpyte.lockscreen.LockScreenImageChanger
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {

  def main(args: Array[String]): Unit = {

    val changer = new LockScreenImageChanger()

    changer.change(Files.newInputStream(Paths.get("C:/tmp/wallpaper.jpg")))

    logger.info("I have won")
  }

}
