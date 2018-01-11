package com.irpyte.lockscreen

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

  def change(image: Path): Unit = {
    Files.copy(image, backgroundFilePath, StandardCopyOption.REPLACE_EXISTING)
  }

}
