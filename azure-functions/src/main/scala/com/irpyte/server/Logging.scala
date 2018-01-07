package com.irpyte.server

import java.util.logging.Logger

trait Logging {

  def logger: Logger = Logging.context

}

object Logging {
  var context: Logger = _
}
