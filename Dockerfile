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

# Copy the renamed JAR and webapp directory with proper permissions
COPY --from=build /app/target/app.jar /app/app.jar
COPY --from=build /app/src/main/webapp /app/webapp

# Copy compiled classes and dependency jars into the exploded webapp so
# Tomcat's webapp classloader can find servlets/filters at runtime.
COPY --from=build /app/target/classes /app/webapp/WEB-INF/classes
COPY --from=build /app/target/dependency /tmp/dependency

# Create necessary directories and verify webapp structure
RUN mkdir -p /app/webapp/WEB-INF && \
    mkdir -p /app/webapp/WEB-INF/lib && \
    # copy dependency jars INTO WEB-INF/lib but EXCLUDE Tomcat/Jasper jars
    if [ -d /tmp/dependency ]; then \
        echo "Copying dependency jars (excluding tomcat/jasper)"; \
        find /tmp/dependency -maxdepth 1 -type f -name '*.jar' \
            ! -iname '*tomcat*.jar' ! -iname '*jasper*.jar' \
            -exec cp {} /app/webapp/WEB-INF/lib/ \; || true; \
    fi && \
    rm -rf /tmp/dependency || true && \
    echo "=== Webapp Contents ===" && \
    ls -la /app/webapp && \
    echo "=== WEB-INF Contents ===" && \
    ls -la /app/webapp/WEB-INF || true && \
    echo "=== All JSP Files ===" && \
    find /app/webapp -name "*.jsp" -type f

# Expose port (Render provides PORT env var at runtime)
EXPOSE 8080

# Run the fat JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

