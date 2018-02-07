package com.irpyte.runner

import java.nio.file.{Files, Path, Paths, StandardOpenOption}

import com.typesafe.scalalogging.LazyLogging
import net.harawata.appdirs.AppDirsFactory

object DB extends LazyLogging {

  val appDirectory: Path = DBRaw.appDirectory
  logger.info(s"App directory: $appDirectory")
  if (!Files.exists(appDirectory)) {
    Files.createDirectories(appDirectory)
  }

  val configFile: Path = appDirectory.resolve("config.json")
  val wallpapersDirectory: Path = appDirectory.resolve("wallpapers")
  if (!Files.exists(wallpapersDirectory)) {
    Files.createDirectory(wallpapersDirectory)
  }

  val usedWallpapersDirectory: Path = appDirectory.resolve("live")
  if (Files.exists(usedWallpapersDirectory)) {
    Files.createDirectory(usedWallpapersDirectory)
  }

  private var appConfig: AppConfig = {
    if (!Files.exists(configFile)) {
      Files.write(configFile, Config.objectMapper.writeValueAsBytes(AppConfig(
        None, None, None, Config.config.getString("defaultUri"), List.empty))
      )
    }
    Config.objectMapper.readValue(Files.newBufferedReader(configFile), classOf[AppConfig])
  }

  def getAppConfig(): AppConfig = synchronized {
    appConfig
  }

  def update(appConfig: AppConfig): Unit = synchronized {
    this.appConfig = appConfig
    val writer = Files.newBufferedWriter(configFile, StandardOpenOption.TRUNCATE_EXISTING)
    Config.objectMapper.writeValue(writer, appConfig)
  }

}
