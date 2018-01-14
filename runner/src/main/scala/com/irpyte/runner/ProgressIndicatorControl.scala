package com.irpyte.runner

import javafx.scene.Node
import javafx.scene.control.{Label, ProgressIndicator}

class ProgressIndicatorControl(progressIndicator: ProgressIndicator, label: Label, nodesToDisable: Node*) {

  def disable(): Unit = {
    nodesToDisable.foreach(_.setDisable(false))
    progressIndicator.setVisible(false)
    label.setVisible(false)

  }

  def enable(message: String): Unit = {
    nodesToDisable.foreach(_.setDisable(true))
    label.setText(message)
    progressIndicator.setVisible(true)
    label.setVisible(true)
  }

  def isRunning(): Boolean = progressIndicator.isVisible

}
