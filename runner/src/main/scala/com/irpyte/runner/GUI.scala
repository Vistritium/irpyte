package com.irpyte.runner

import java.nio.file.Files
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.TextField
import javafx.scene.image.{Image, ImageView}
import javafx.scene.{Parent, Scene}
import javafx.scene.layout.{AnchorPane, HBox, VBox}
import javafx.stage.Stage

import com.irpyte.lockscreen.DesktopWallpaperImageChanger
import com.typesafe.scalalogging.LazyLogging

class GUI extends Application with LazyLogging {

  val changer = new DesktopWallpaperImageChanger

  override def start(stage: Stage): Unit = {
    val loader = new FXMLLoader(getClass.getClassLoader.getResource("scene.fxml"))
    loader.setController(this)
    val parent = loader.load().asInstanceOf[VBox]

    stage.show()
    stage.setScene(new Scene(parent))
    stage.setTitle("FXML Welcome")

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
    if(text.trim.nonEmpty){
      WallpapersService.createNew(text)
    }
    logger.info("onSearch")
  }


}
