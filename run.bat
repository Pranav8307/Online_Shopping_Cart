@echo off
echo ================================================
echo Online Shopping Cart System - Starting...
echo ================================================
echo.

REM Check if Maven wrapper exists
if exist "mvnw.cmd" (
    echo Using Maven Wrapper...
    call mvnw.cmd clean compile exec:java
) else (
    echo Maven Wrapper not found. Checking for Maven...
    where mvn >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo Using system Maven...
        mvn clean compile exec:java
    ) else (
        echo.
        echo ERROR: Maven is not installed and Maven wrapper is not available.
        echo Please install Maven from https://maven.apache.org/download.cgi
        echo Or ensure mvnw.cmd is in the project directory.
        echo.
        pause
        exit /b 1
    )
)

pause

