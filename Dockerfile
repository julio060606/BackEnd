# Build Stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml ./
# If maven wrapper exists, copy it, otherwise install maven or build with jar
RUN apt-get update && apt-get install -y maven

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create unprivileged user for security
RUN groupadd -r chasquifly && useradd -r -g chasquifly chasquifly
USER chasquifly

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
