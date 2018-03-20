package com.irpyte.server.services

import com.irpyte.server.Config

object VersionController {

  case class VersionResponse(newestVersion: String, url: String, needToUpdate: Boolean)

  def check(usersVersion: String): VersionResponse = {
    val currentVersionFloat = Config.config.currentVersion.toFloat
    val usersVersionFloat = try {
      usersVersion.toDouble
    } catch {
      case _: NumberFormatException => throw new IllegalStateException(s"Invalid version number: $usersVersion")
    }

    val needToUpdate = currentVersionFloat > usersVersionFloat

    VersionResponse(Config.config.currentVersion, Config.config.linkToApp, needToUpdate)
  }

}
