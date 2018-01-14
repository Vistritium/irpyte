package com.irpyte.runner

import java.nio.file.Files
import java.util.concurrent.{Executors, TimeUnit}
import javafx.application.{Application, Platform}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Label, ProgressIndicator, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.VBox
import javafx.stage.Stage

import com.irpyte.lockscreen.DesktopWallpaperImageChanger
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

class GUI extends Application with LazyLogging {
  private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
  private val changer = new DesktopWallpaperImageChanger

  var progressIndicatorControl: ProgressIndicatorControl = null

  override def start(stage: Stage): Unit = {
    Platform.setImplicitExit(false)

    val loader = new FXMLLoader(getClass.getClassLoader.getResource("scene.fxml"))
    loader.setController(this)
    val parent = loader.load().asInstanceOf[VBox]
    stage.setResizable(false)

    val image = new Image("/icon.png")
    stage.getIcons.add(image)
    Tray.createTrayIcon(stage, this)

    stage.setScene(new Scene(parent))
    stage.setTitle("Irpyte")

    progressIndicatorControl = new ProgressIndicatorControl(progressIndicator, progressLabel, controlsContainer)
    progressIndicatorControl.disable()

    DB.getAppConfig().searchTerms.foreach(search => {
      searchText.setText(search)
    })

    scheduledExecutorService.scheduleAtFixedRate(() => handleNext(), 0, 1, TimeUnit.DAYS)
    scheduledExecutorService.scheduleAtFixedRate(() => WallpapersService.tick(), 0, 1, TimeUnit.HOURS)


  }


  @FXML private var searchText: TextField = null

  @FXML private var image: ImageView = null

  @FXML def onNext(event: ActionEvent): Unit = {
    handleNext()
  }

  def handleNext(): Unit = {
    progressIndicatorControl.enable("Getting new wallpaper")
    scheduledExecutorService.execute(() => {
      Try {
        WallpapersService.getNextWallpaper().foreach(path => {
          changer.change(path)
          image.setImage(new Image(Files.newInputStream(path)))
        })
      } match {
        case Failure(exception) => logger.error("Error while getting next wallpaper", exception)
        case Success(_) =>
      }
      Platform.runLater(() => {
        progressIndicatorControl.disable()
      })
    })

    scheduledExecutorService.execute(() => {
      WallpapersService.tick()
    })

  }

  @FXML def search(event: ActionEvent): Unit = {
    logger.info("onSearch")
    progressIndicatorControl.enable("Searching for new wallpapers")

    scheduledExecutorService.execute(() => {
      Try {
        val text = searchText.getText
        if (text.trim.nonEmpty) {
          WallpapersService.createNew(text)
        }
      } match {
        case Failure(exception) => logger.error(s"Error while creating new wallpaper", exception)
        case Success(_) =>
      }

      Platform.runLater(() => {
        progressIndicatorControl.disable()
      })
    })

  }

  @FXML var controlsContainer: Parent = null


  @FXML
  private var progressIndicator: ProgressIndicator = null

  @FXML
  private var progressLabel: Label = null


}
