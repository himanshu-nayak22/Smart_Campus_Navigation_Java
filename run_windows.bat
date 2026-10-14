@echo off
setlocal
if not exist out mkdir out
javac -d out src\main\java\com\smartcampus\*.java
if errorlevel 1 (
  echo.
  echo Compilation failed. Check the error messages above.
  pause
  exit /b 1
)
java -cp out com.smartcampus.Main
pause
