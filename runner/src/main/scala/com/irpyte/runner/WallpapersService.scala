package com.irpyte.runner

import java.io.IOException
import java.nio.file.{Files, Path, StandardCopyOption}

import com.irpyte.connect.IrpyteConnect
import com.typesafe.scalalogging.LazyLogging
import okhttp3._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import resource._

object WallpapersService extends LazyLogging {

  val connect = new IrpyteConnect(DB.getAppConfig().wallpaperUri)
  val client = new OkHttpClient()

  def createNew(search: String): Unit = {
    val response = connect.create(search)
    DB.update(DB.getAppConfig().copy(wallpaperId = Some(response.id), imageFilenames = List.empty, searchTerms = Some(search)))
    logger.info(s"Updated id with ${response.id}")
  }

  def tick(): Unit = this.synchronized {
    logger.info("tick")
    DB.getAppConfig().wallpaperId.foreach(id => {
      if (DB.getAppConfig().imageFilenames.lengthCompare(5) < 0) {
        downloadNewImages(id)
      }
    })

  }

  def getNextWallpaper(): Option[Path] = this.synchronized {

    DB.getAppConfig().wallpaperId.flatMap(id => {

      val filenames = DB.getAppConfig().imageFilenames

      if (filenames.isEmpty) {
        logger.info("No more images, will try to download")
        downloadNewImages(id)
      }

      if (DB.getAppConfig().imageFilenames.isEmpty) {
        logger.info("No images, returning")
        None
      } else {
        val filenames = DB.getAppConfig().imageFilenames
        val head :: tail = filenames

        DB.update(DB.getAppConfig().copy(imageFilenames = tail))

        logger.info(s"Changing to $head")
        Some(DB.wallpapersDirectory.resolve(head))
      }
    })

  }

  private def downloadNewImages(id: String): Unit = {
    logger.info("downloadNewImages")
    val response = connect.wallpaper(id)

    if (response.imageResults.isEmpty) {
      logger.info("Search resulted in 0 images")
    }

    val futures = response.imageResults.map(entry => {
      val request = new Request.Builder()
        .url(entry.url)
        .build()

      val promise = Promise[Path]()

      client.newCall(request).enqueue(new Callback {
        override def onResponse(call: Call, response: Response): Unit = {
          for (response <- managed(response)) {
            if (response.isSuccessful) {
              Try {
                val extension = entry.url.split('.').last
                val file = DB.wallpapersDirectory.resolve(s"${entry.id}.$extension")
                Files.copy(response.body().byteStream(), file, StandardCopyOption.REPLACE_EXISTING)
                logger.info(s"Downloaded $file")
                file
              } match {
                case Failure(exception) => {
                  logger.error(s"error for ${entry.url}", exception)
                  promise.failure(exception)
                }
                case Success(value) => promise.success(value)
              }
            } else {
              val errorStr = s"Not successful response: for uri ${entry.url} : " +
                s"${response.body().string()}"
              logger.error(errorStr)
              promise.failure(new IllegalStateException(errorStr))
            }
          }
        }

        override def onFailure(call: Call, e: IOException): Unit = {
          logger.error(s"Error while making a call: ${entry.url}", e)
          promise.failure(e)
        }
      })

      promise.future
    })

    val recovered = futures
      .map(_.map(Some(_)))
      .map(_.recover({
        case e => None
      }))

    val eventualPaths = Future.sequence(recovered).map(_.flatten)

    val paths = Await.result(eventualPaths, 10 minutes)

    logger.info(s"Dowloaded ${paths.size} images")

    DB.update(DB.getAppConfig().copy(imageFilenames = DB.getAppConfig().imageFilenames ::: paths.map(_.getFileName.toString)))
  }


}
