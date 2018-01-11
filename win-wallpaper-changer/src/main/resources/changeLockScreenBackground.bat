if not exist "C:\Windows\System32\oobe\info" mkdir C:\Windows\System32\oobe\info
if not exist "C:\Windows\System32\oobe\info\backgrounds" mkdir C:\Windows\System32\oobe\info\backgrounds

copy %1 C:\Windows\System32\oobe\info\backgrounds\defaultBackground.jpg /y