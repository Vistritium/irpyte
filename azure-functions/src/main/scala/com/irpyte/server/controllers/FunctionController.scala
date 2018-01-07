package com.irpyte.server.controllers

import com.irpyte.server.Logging
import com.microsoft.azure.serverless.functions.{ExecutionContext, HttpRequestMessage, HttpResponseMessage}
import org.apache.commons.lang3.exception.ExceptionUtils

object FunctionController {

  def wrap(context: ExecutionContext, request: HttpRequestMessage[_],
    fun: () => HttpResponseMessage[String]): HttpResponseMessage[String] = {
    Logging.context = context.getLogger
    try {
      fun.apply()
    } catch {
      case e: Exception => {
        request.createResponse(500, "Error happened" + e.getMessage)
      }
      case e: Throwable => {
        val str = ExceptionUtils.getStackTrace(e)
        context.getLogger.severe("Throwable: " + e.getMessage + "\n" + str)
        throw e
      }
    }
  }

}
