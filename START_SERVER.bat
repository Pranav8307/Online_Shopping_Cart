@echo off
echo ================================================
echo Starting Online Shopping Cart System...
echo ================================================
echo.

REM Set JAVA_HOME to the detected Java installation
set JAVA_HOME=C:\Program Files\Java\jdk-23

REM Verify Java exists
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo ERROR: Java not found at %JAVA_HOME%
    echo Please check your Java installation.
    pause
    exit /b 1
)

echo Java found at: %JAVA_HOME%
echo.

echo This will take a few minutes on first run...
echo Maven will download dependencies automatically.
echo.
echo Please wait and watch for:
echo "Online Shopping Cart System is running!"
echo.
echo Then open: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo ================================================
echo.

cd /d "%~dp0"
cmd /c mvnw.cmd clean compile exec:java

pause
