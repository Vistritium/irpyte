package com.irpyte.runner

case class AppConfig(
                      wallpaperId: Option[String],
                      wallpaperUri: String,
                      imageFilenames: List[String]
                    )

