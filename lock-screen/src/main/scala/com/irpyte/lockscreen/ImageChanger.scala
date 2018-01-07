package com.irpyte.lockscreen

import java.io.InputStream

trait ImageChanger {
  def change(inputStream: InputStream): Unit
}
