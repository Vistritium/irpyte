package com.irpyte.runner

import java.nio.file.{Files, Path, Paths}

object DBRaw {

  val appDirectory: Path = Paths.get(System.getenv("LOCALAPPDATA")).resolve("irpyte")
  if (!Files.exists(appDirectory)) {
    Files.createDirectories(appDirectory)
  }

}
