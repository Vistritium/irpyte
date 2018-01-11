package com.irpyte.connect

import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import okhttp3._
import resource.managed


class IrpyteConnect(address: String) {

  private val objectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
  }

  private val client = new OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .readTimeout(80, TimeUnit.SECONDS)
    .build();
  private val createPath = address + "/api/create"
  private val wallpaperPath = address + "/api/wallpaper"

  def create(search: String): CreateResponse = {
    val searchForm = CreateForm(search)

    val body = RequestBody.create(IrpyteConnect.MediaType,
      objectMapper.writeValueAsBytes(
        searchForm
      ))

    val request = new Request.Builder()
      .url(createPath)
      .post(body)
      .build()

    throwIfNotSuccess(request) { response =>
      objectMapper.readValue(response.body().byteStream(), classOf[CreateResponse])
    }

  }

  def wallpaper(id: String): WallpaperResponse = {
    val request = new Request.Builder()
      .url(s"$wallpaperPath/$id")
      .build()

    throwIfNotSuccess(request) {
      response => objectMapper.readValue(response.body().byteStream(), classOf[WallpaperResponse])
    }
  }

  private def throwIfNotSuccess[T](request: Request)(read: (Response => T)): T = {
    val result = managed(client.newCall(request).execute()).map(response => {
      if (!response.isSuccessful) {
        throw new IllegalStateException(s"Unsuccessfull response ${request.url().toString} ${response.code()} ${response.body().string()}")
      } else {
        read.apply(response)
      }
    }).either
    result.either.getOrElse(throw new RuntimeException(result.either.left.get.head))
  }

  private case class CreateForm(search: String)


}

case class CreateResponse(id: String)
case class WallpaperResponse(imageResults: List[WallpaperResponseEntry])
case class WallpaperResponseEntry(id: String, url: String, imageType: String)

object IrpyteConnect {
  private val MediaType: MediaType = okhttp3.MediaType.parse("application/json; charset=utf-8")
}
