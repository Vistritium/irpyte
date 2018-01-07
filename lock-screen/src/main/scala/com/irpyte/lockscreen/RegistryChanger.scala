package com.irpyte.lockscreen

import com.typesafe.scalalogging.LazyLogging

private[lockscreen] object RegistryChanger extends LazyLogging {

  private val commandExecute =
    """REG ADD "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Authentication\LogonUI\Background" /v OEMBackground /t REG_DWORD /d 1 /f"""
      .split(" ")

  def enableOEMImages(): Unit = {
    CommandExecutor.execute(commandExecute)
  }

}
