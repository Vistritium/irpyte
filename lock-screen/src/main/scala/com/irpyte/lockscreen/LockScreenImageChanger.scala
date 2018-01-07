package com.irpyte.lockscreen

import java.io.InputStream
import java.nio.file.{Files, Path, Paths, StandardCopyOption}

class LockScreenImageChanger extends ImageChanger {
  init()

  private val backgroundFilePath = Paths.get("C:/Windows/System32/oobe/info/backgrounds/backgroundDefault.jpg")

  def init(): Unit = {
    RegistryChanger.enableOEMImages()
    Seq {
      "C:/Windows/System32/oobe/info"
      "C:/Windows/System32/oobe/info/backgrounds"
    }.map(Paths.get(_))
      .foreach(createDirIfNotExists)
  }


  private def createDirIfNotExists(path: Path) = {
    if (!Files.exists(path)) {
      Files.createDirectory(path)
    }
  }

  def change(inputStream: InputStream): Unit = {
    Files.copy(inputStream, backgroundFilePath, StandardCopyOption.REPLACE_EXISTING)
  }

}
