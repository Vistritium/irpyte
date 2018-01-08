package com.irpyte.server.wallpaperprovider

import com.fasterxml.jackson.annotation.JsonProperty
import com.irpyte.server.data.{AppSettings, UserData}
import com.irpyte.server.{Config, Logging}
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.util.EntityUtils

import scala.util.Random

object AlphacodersWallpaperProvider extends WallpaperProvider with Logging {

  private val uriBase = """https://wall.alphacoders.com/api2.0/get.php"""

  def searchRequest(page: Int, search: String, appSettings: AppSettings): AlphacodersSearchResponse = {
    val uri = new URIBuilder(uriBase)
      .addParameter("auth", appSettings.alphacodersApikey)
      .addParameter("method", "search")
      .addParameter("term", search)
      .addParameter("page", page.toString)
      .build()
    val get = new HttpGet(uri)
    logger.info(s"Perfomring request $uri")
    val response = Config.httpClient.execute(get)

    try {
      if (response.getStatusLine.getStatusCode != 200) {
        throw new IllegalStateException(s"Not successfull response code ${response.getStatusLine} for query $uri")
      }
      Config.objectMapper.readValue(response.getEntity.getContent, classOf[AlphacodersSearchResponse])
    } finally {
      EntityUtils.consume(response.getEntity)
    }
  }

  case class AlphacodersSearchResponse(
                                        wallpapers: List[AlphacodersSearchResponseEntry],
                                        @JsonProperty("total_match") totalMatch: Int
                                      )
  case class AlphacodersSearchResponseEntry(
                                             id: String,
                                             width: Int,
                                             height: Int,
                                             @JsonProperty("file_type") fileType: String,
                                             @JsonProperty("url_image") urlImage: String,
                                             @JsonProperty("url_thumb") urlThumb: String,
                                             @JsonProperty("url_page") urlPage: String
                                           )

  override def search(search: String, userData: UserData, appSettings: AppSettings): SearchResult = {
    logger.info(s"Searching for $search with ${userData.usedIds.length} already used")
    val initialResponse = searchRequest(1, search, appSettings)

    if (initialResponse.totalMatch == 0) {
      SearchResult(Seq.empty, userData)
    } else {
      val maxIter = 20
      val desiredResultSize = 5

      def loop(aggregate: List[ImageResult], iter: Int, pages: List[Int]): List[ImageResult] = {
        if (iter > maxIter) {
          logger.warning("Max iter reached")
          aggregate
        }
        else if (aggregate.size >= desiredResultSize) {
          logger.info(s"Found desired size after $iter iterations")
          aggregate.take(desiredResultSize)
        }
        else if (pages.isEmpty) {
          logger.info("Ran out of pages")
          aggregate
        } else {
          val currentPage :: remainingPages = pages
          val entries = searchResponseToImageResults(searchRequest(currentPage, search, appSettings))
          val filtered = filterUsedImages(entries, userData)
          val nextAggregate = filtered ::: aggregate
          logger.info(s"Harvested ${filtered.size} pages out of ${entries.size}. Currently got ${nextAggregate.size}")
          loop(nextAggregate, iter + 1, remainingPages)
        }
      }

      val amountOfPages = initialResponse.totalMatch / 10
      val resultEntries = loop(
        filterUsedImages(searchResponseToImageResults(initialResponse), userData), 0,
        Random.shuffle((1 until amountOfPages).toList)
      )

      SearchResult(
        resultEntries,
        userData.copy(usedIds = resultEntries.map(_.id) ::: userData.usedIds)
      )
    }
  }

  private def filterUsedImages(entries: List[ImageResult], userData: UserData) = {
    entries.filter(entry => !userData.usedIds.contains(entry.id))
  }

  private def searchResponseToImageResults(searchResponse: AlphacodersSearchResponse): List[ImageResult] = {
    searchResponse.wallpapers.map(entry => ImageResult(entry.id, entry.urlImage, entry.urlImage))
  }


}
