package com.irpyte.runner

import java.awt.event.ActionEvent
import java.awt.{Image, MenuItem, PopupMenu}
import java.nio.file.Paths
import javafx.stage.WindowEvent

import com.typesafe.scalalogging.LazyLogging

object Tray extends LazyLogging {

  import java.awt.TrayIcon

  private var trayIcon: TrayIcon = _

  import java.awt.event.ActionListener
  import java.awt.{SystemTray, TrayIcon}
  import javafx.application.Platform
  import javafx.stage.Stage
  import javax.imageio.ImageIO

  def createTrayIcon(stage: Stage, gui: GUI): Unit = {
    if (SystemTray.isSupported) { // get the SystemTray instance
      val tray = SystemTray.getSystemTray
      // load an image
      val image = ImageIO.read(getClass.getResourceAsStream("/tray.png"))

      stage.setOnCloseRequest((t: WindowEvent) => {
        hide(stage)
      })

      // create a action listener to listen for default action executed on the tray icon
      val closeListener = new ActionListener() {
        override def actionPerformed(e: ActionEvent): Unit = {
          Platform.exit()
          System.exit(0)
        }
      }

      val changeWallpaperListener = new ActionListener() {
        override def actionPerformed(e: ActionEvent): Unit = {
          Platform.runLater(() => {
            gui.handleNext()
          })
        }
      }

      // create a popup menu
      val popup = new PopupMenu()
      val changeWallpaper = new MenuItem("Change wallpaper")
      changeWallpaper.addActionListener(changeWallpaperListener)
      popup.add(changeWallpaper)
      val closeItem = new MenuItem("Close")
      closeItem.addActionListener(closeListener)
      popup.add(closeItem)
      /// ... add other items
      // construct a TrayIcon
      trayIcon = new TrayIcon(image, "Irpyte", popup)
      // set the TrayIcon properties
      trayIcon.addActionListener((e: ActionEvent) => {
        logger.info("Click tray")
        Platform.runLater(() => {
          stage.show()
          DB.getAppConfig().currentWallpaperPath.foreach(uri => {
            gui.setImage(Paths.get(uri))
          })
        })
      })

      // ...
      // add the tray image
      tray.add(trayIcon)

    }

    def hide(stage: Stage): Unit = {
      Platform.runLater(() => {
        if (SystemTray.isSupported) {
          stage.hide()
          gui.clearImage()
          System.gc()
        }
        else System.exit(0)
      })
    }
  }


}
