package com.irpyte.server

import java.io.InputStreamReader

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  val config: Config = {
    val stream = getClass.getResourceAsStream("app.conf")
    ConfigFactory.parseReader(new InputStreamReader(stream))
  }

}
