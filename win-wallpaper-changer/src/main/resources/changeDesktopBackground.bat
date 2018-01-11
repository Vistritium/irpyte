reg add "HKCU\Control Panel\Desktop" /v Wallpaper /f /t REG_SZ /d %1
rem rundll32.exe user32.dll, UpdatePerUserSystemParameters
RUNDLL32.EXE USER32.DLL,UpdatePerUserSystemParameters ,1 ,True
