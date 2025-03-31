# Use a base image with JDK 17 (since you have Java 17)
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
