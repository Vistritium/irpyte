package com.irpyte.runner

case class AppConfig(
                      wallpaperId: Option[String],
                      searchTerms: Option[String],
                      currentWallpaperPath: Option[String],
                      wallpaperUri: String,
                      imageFilenames: List[String],
                      wasAutoStartSet: Boolean = false
                    )

