package com.irpyte.runner

import java.lang.management.ManagementFactory
import java.nio.file.{Files, Path, Paths}

import com.github.sarxos.winreg.{HKey, WindowsRegistry}
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConverters._
import scala.util.Try
import scala.util.control.NonFatal

object SystemUtils extends LazyLogging {

  def getCommandLine(): Unit = {
    logger.info("getLibraryPath path: " + ManagementFactory.getRuntimeMXBean().getLibraryPath)
    logger.info("getBootClassPath path: " + ManagementFactory.getRuntimeMXBean().getBootClassPath)
    logger.info("getName path: " + ManagementFactory.getRuntimeMXBean().getName)
    logger.info("getInputArguments path: " + ManagementFactory.getRuntimeMXBean().getInputArguments.asScala.mkString(", "))
    logger.info("getClassPath path: " + ManagementFactory.getRuntimeMXBean().getClassPath)
    logger.info("getClass().getProtectionDomain().getCodeSource().getLocation(): " + this.getClass().getProtectionDomain().getCodeSource().getLocation())
  }

  def checkStartup(): Unit = {
    if (!DB.getAppConfig().wasAutoStartSet) {
      logger.info("Trying to set autostart")
      try {
        setAsStartup()
      } catch {
        case NonFatal(e) => logger.error("Something wrong while setting app on startup", e)
      }

    }
  }

  //noinspection IfElseToFilterdOption
  def setAsStartup(): Unit = {

    def findExeWithingPaths(paths: List[Path]): Option[Path] = {
      paths.flatMap(x => {
        Try {
          logger.debug(s"Trying to find exe in $x")
          Files.newDirectoryStream(x, "irpyte.exe").iterator().asScala.toList.head
        }.toOption
      }).headOption
    }

    def findIrpytePaths(remaining: List[Path], found: List[Path]): List[Path] = {
      val head :: tail = remaining
      val isCurrentHeadOk = head.endsWith("irpyte")

      val nextFound = if (isCurrentHeadOk) {
        head :: found
      } else {
        found
      }

      val nextRemaining = Option(head.getParent) -> isCurrentHeadOk match {
        case (_, true) => tail
        case (Some(value), _) => value :: tail
        case _ => tail
      }

      if (nextRemaining.isEmpty) {
        found
      } else {
        findIrpytePaths(nextRemaining, nextFound)
      }
    }

    val pathToCheck = ManagementFactory.getRuntimeMXBean.getLibraryPath.split(';').flatMap(path => {
      Try {
        val maybe = Paths.get(path)
        if (Files.exists(maybe)) {
          Some(maybe)
        } else {
          None
        }
      }.toOption
    }).flatten

    val paths = findIrpytePaths(pathToCheck.toList, List.empty)
    if (paths.nonEmpty) {
      findExeWithingPaths(paths) match {
        case Some(value) => {
          logger.info(s"Found exe path: $value")
          WindowsRegistry.getInstance().writeStringValue(HKey.HKCR, "Software\\Microsoft\\Windows\\CurrentVersion\\Run",
            "Irpyte",
            s"$value autostart")
          DB.update(DB.getAppConfig().copy(wasAutoStartSet = true))
        }
        case None => logger.error(s"Couldn't find exe file in paths: \n${paths.map(_.toAbsolutePath).mkString("\n")}")

      }
    } else {
      logger.error(s"Couldn't find irpye dir paths from :\n${pathToCheck.mkString("\n")}")
    }


  }

}
