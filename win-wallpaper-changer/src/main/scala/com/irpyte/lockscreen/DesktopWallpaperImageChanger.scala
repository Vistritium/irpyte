package com.irpyte.lockscreen

import java.nio.file.{Files, Path, StandardCopyOption}

class DesktopWallpaperImageChanger extends ImageChanger {

  override def change(imageLocation: Path): Unit = {
    val tmpFile = Files.createTempFile("changeDesktopBackground", ".cmd")
    Files.copy(getClass.getResourceAsStream("/changeDesktopBackground.bat"), tmpFile, StandardCopyOption.REPLACE_EXISTING)
    CommandExecutor.execute(Seq("cmd.exe", "/c", s"""call "$tmpFile" "${imageLocation.toAbsolutePath.toString}""""))
    Files.delete(tmpFile)
  }


}
