# Quick Start Guide

## Running the Application

### Option 1: Using the Batch File (Easiest)
1. Double-click `START_SERVER.bat` in Windows Explorer
2. Wait for Maven to download dependencies (first time only - takes 2-5 minutes)
3. Look for the message: "Online Shopping Cart System is running!"
4. Open your browser and go to: **http://localhost:8080**

### Option 2: Using Command Prompt
1. Open Command Prompt (not PowerShell)
2. Navigate to the project directory:
   ```
   cd "C:\Users\prana\OneDrive\Desktop\JAVA Project"
   ```
3. Run:
   ```
   mvnw.cmd clean compile exec:java
   ```
4. Wait for the server to start
5. Open: **http://localhost:8080**

### Option 3: Using PowerShell
1. Open PowerShell
2. Navigate to the project directory
3. Run:
   ```
   cmd /c mvnw.cmd clean compile exec:java
   ```
4. Wait for the server to start
5. Open: **http://localhost:8080**

## Default Login Credentials

**Admin:**
- Username: `admin`
- Password: `admin123`

**Customer:**
- Username: `customer`
- Password: `customer123`

## Troubleshooting

### Port 8080 Already in Use
If you get an error about port 8080 being in use:
1. Find what's using it: `netstat -ano | findstr :8080`
2. Stop that application
3. Or change the port in `src/main/java/com/shoppingcart/Application.java`

### Maven Wrapper Issues
If `mvnw.cmd` doesn't work:
1. Install Maven from: https://maven.apache.org/download.cgi
2. Add Maven to your PATH
3. Then use `mvn` instead of `mvnw.cmd`

### First Run Takes Long Time
This is normal! Maven needs to download:
- Maven itself (~5MB)
- All project dependencies (~50-100MB)
- This only happens once

## Stopping the Server
Press `Ctrl+C` in the terminal where it's running

