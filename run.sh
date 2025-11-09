#!/bin/bash

echo "================================================"
echo "Online Shopping Cart System - Starting..."
echo "================================================"
echo ""

# Check if Maven wrapper exists
if [ -f "mvnw" ]; then
    echo "Using Maven Wrapper..."
    ./mvnw clean compile exec:java
elif command -v mvn &> /dev/null; then
    echo "Using system Maven..."
    mvn clean compile exec:java
else
    echo ""
    echo "ERROR: Maven is not installed and Maven wrapper is not available."
    echo "Please install Maven from https://maven.apache.org/download.cgi"
    echo "Or ensure mvnw is in the project directory."
    echo ""
    exit 1
fi

