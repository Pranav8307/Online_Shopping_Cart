Write-Host "================================================" -ForegroundColor Cyan
Write-Host "Online Shopping Cart System - Starting..." -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven wrapper exists
if (Test-Path "mvnw.cmd") {
    Write-Host "Using Maven Wrapper..." -ForegroundColor Yellow
    & cmd /c mvnw.cmd clean compile exec:java
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Using system Maven..." -ForegroundColor Yellow
    mvn clean compile exec:java
} else {
    Write-Host ""
    Write-Host "ERROR: Maven is not installed and Maven wrapper is not available." -ForegroundColor Red
    Write-Host ""
    Write-Host "Please choose one of the following options:" -ForegroundColor Yellow
    Write-Host "1. Install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "2. Or download Maven wrapper manually" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To install Maven wrapper, run:" -ForegroundColor Cyan
    Write-Host '  Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar" -OutFile ".mvn\wrapper\maven-wrapper.jar"' -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

