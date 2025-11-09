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
RUN mvn clean package -DskipTests && \
    mv target/online-shopping-cart-1.0.0.jar target/app.jar

# Runtime stage - Use Temurin JRE for smaller runtime image
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy the renamed JAR and webapp directory
COPY --from=build /app/target/app.jar /app/app.jar
COPY --from=build /app/src/main/webapp /app/webapp

# Expose port (Render provides PORT env var at runtime)
EXPOSE 8080

# Run the fat JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

