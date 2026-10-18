# Build Stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create unprivileged user for security
RUN groupadd -r chasquifly && useradd -r -g chasquifly chasquifly

# Copy jar from build stage with correct permissions
COPY --from=build --chown=chasquifly:chasquifly /app/target/*.jar app.jar

USER chasquifly

EXPOSE 8080

# Configure memory limits for Render free tier (512 MB RAM)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
