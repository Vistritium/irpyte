package com.irpyte.server.services

import com.irpyte.server.data.{AppSettings, UserData}
import com.irpyte.server.wallpaperprovider.{AlphacodersWallpaperProvider, SearchResult, WallpaperProvider}

object WallpaperService {

  val provider: WallpaperProvider = AlphacodersWallpaperProvider

  def searchImages(search: String, userData: UserData, appSettings: AppSettings): SearchResult = {
    provider.search(search, userData, appSettings)
  }

}
