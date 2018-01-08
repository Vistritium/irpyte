package com.irpyte.server

import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.irpyte.server.data.AppSettings
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

object Config {

  var config: AppSettings = _

  def initConfig(data: String): Unit = {
    this.config = objectMapper.readValue(data.getBytes(StandardCharsets.UTF_8), classOf[AppSettings])
  }


  val objectMapper: ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper
  }

  lazy val httpClient: CloseableHttpClient = HttpClients.createDefault()

}
