package com.irpyte.server.wallpaperprovider

import com.irpyte.server.data.{AppSettings, UserData}

trait WallpaperProvider {

  def search(search: String, userData: UserData, appSettings: AppSettings): SearchResult

}

case class SearchResult(results: Seq[ImageResult], updatedUserData: UserData)
case class ImageResult(id: String, url: String, imageType: String)
