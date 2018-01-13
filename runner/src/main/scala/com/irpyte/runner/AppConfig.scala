package com.irpyte.runner

case class AppConfig(
                      wallpaperId: Option[String],
                      searchTerms: Option[String],
                      wallpaperUri: String,
                      imageFilenames: List[String]
                    )

