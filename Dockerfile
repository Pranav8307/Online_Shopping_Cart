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

# Copy dependencies from build stage
COPY --from=build /app/target/dependency ./dependency
# Copy compiled classes
COPY --from=build /app/target/classes ./classes
# Copy webapp directory to the correct location
COPY --from=build /app/src/main/webapp ./src/main/webapp

# Expose port (Render will set PORT env var)
EXPOSE 8080

# Run the application
CMD ["java", "-cp", "classes:dependency/*", "com.shoppingcart.Application"]

