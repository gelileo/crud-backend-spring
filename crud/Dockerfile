# Use an official OpenJDK runtime as a parent image
#FROM adoptopenjdk/openjdk17:alpine-jre
FROM arm64v8/eclipse-temurin:17-jdk-focal
# Set the working directory to /app
WORKDIR /app

# Copy the executable JAR file and any dependencies into the container
COPY target/crud-0.0.1-SNAPSHOT.jar /app

# Expose the port that the Spring Boot application will listen on
EXPOSE 8081

# Set the entry point for the container to run the executable JAR file
ENTRYPOINT ["java", "-jar", "crud-0.0.1-SNAPSHOT.jar"]