package com.irpyte.lockscreen

import java.nio.file.Path

trait ImageChanger {
  def change(imageLocation: Path): Unit
}
