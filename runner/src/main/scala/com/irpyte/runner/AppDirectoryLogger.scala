package com.irpyte.runner

import ch.qos.logback.core.FileAppender

class AppDirectoryLogger extends FileAppender {

  override def getFile: String = {
    DBRaw.appDirectory.resolve("app.log").toAbsolutePath.toString
  }

  override def isAppend: Boolean = false


}
