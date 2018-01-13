package com.irpyte.runner

import java.nio.file.Files
import java.util.concurrent.{ExecutorService, Executors, TimeUnit}
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.VBox
import javafx.stage.Stage

import com.irpyte.lockscreen.{DesktopWallpaperImageChanger, LockScreenImageChanger}
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

class GUI extends Application with LazyLogging {
  private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
  private val changer = new DesktopWallpaperImageChanger

  override def start(stage: Stage): Unit = {
    val loader = new FXMLLoader(getClass.getClassLoader.getResource("scene.fxml"))
    loader.setController(this)
    val parent = loader.load().asInstanceOf[VBox]

    stage.show()
    stage.setScene(new Scene(parent))
    stage.setTitle("Irypyte")

    DB.getAppConfig().searchTerms.foreach(search => {
      searchText.setText(search)
    })

    scheduledExecutorService.scheduleAtFixedRate(() => WallpapersService.tick(), 0, 1, TimeUnit.HOURS)
  }


  @FXML private var searchText: TextField = null

  @FXML private var image: ImageView = null

  @FXML def onNext(event: ActionEvent): Unit = {
    WallpapersService.getNextWallpaper().foreach(path => {
      changer.change(path)
      image.setImage(new Image(Files.newInputStream(path)))
    })
    logger.info("onNext")
  }

  @FXML def search(event: ActionEvent): Unit = {

    val text = searchText.getText
    if (text.trim.nonEmpty) {
      WallpapersService.createNew(text)
    }
    logger.info("onSearch")
  }


}
