# Build stage - Use Temurin JDK for Maven build
FROM eclipse-temurin:11-jdk AS build
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - Use Temurin JRE for smaller runtime image
FROM eclipse-temurin:11-jre
WORKDIR /app

## For simplicity produce a fat JAR (built by maven-shade) and run it.
## Build stage already ran `mvn clean package -DskipTests` which will produce
## target/*-shaded.jar (maven-shade default) or a jar in target.

# Copy the produced JAR from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose port (Render provides PORT env var at runtime)
EXPOSE 8080

# Run the fat JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

