package com.irpyte.server.controllers

import com.irpyte.server.{Config, Logging}
import com.microsoft.azure.serverless.functions.{ExecutionContext, HttpRequestMessage, HttpResponseMessage}
import org.apache.commons.lang3.exception.ExceptionUtils

object FunctionController {

  @throws(classOf[Exception])
  def wrap[AnyRef](context: ExecutionContext, request: HttpRequestMessage[_], config: String,
                   fun: () => HttpResponseMessage[AnyRef]
                  ): HttpResponseMessage[AnyRef] = {
    Logging.context = context.getLogger
    Config.initConfig(config)
    try {
      fun.apply()
    } catch {
      case e: Exception => {
        Logging.context.severe(ExceptionUtils.getStackTrace(e))
        request.createResponse(500, ("Error happened" + e.getMessage).asInstanceOf[AnyRef])
      }
      case e: Throwable => {
        val str = ExceptionUtils.getStackTrace(e)
        context.getLogger.severe("Throwable: " + e.getMessage + "\n" + str)
        throw e
      }
    }
  }

  def readContent(request: HttpRequestMessage[Array[Byte]]): Unit = {

  }
}
