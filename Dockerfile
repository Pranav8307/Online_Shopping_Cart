# Build stage
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:11-jdk-slim
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

