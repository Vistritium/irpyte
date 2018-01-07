package com.irpyte.server.services

import scala.util.Random

object NameUtil {

  def trollName(s: String): String = {
    Random.shuffle(s.grouped(1)).mkString
  }

}
