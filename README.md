# Devices API

This is a Spring Boot-based API for managing devices, containerized with Docker and using MySQL as a database.

## Prerequisites

- Docker and Docker Compose installed
- Java 23 (as specified in `build.gradle`)
- Gradle installed (or use the included `gradlew` script)

## Setup and Running the Application

### Running Locally (Without Docker)

1. Clone the repository:

   ```sh
   git clone https://github.com/JohnMarley/devices-api.git
   ```

### Running with Docker

1. Ensure Docker is installed and running.

2. Execute command in the project root folder
   ```sh
   ./gradlew bootJar
   ```
   keep in mind that for cmd command on Windows you should use
   ```cmd
   .\gradlew bootJar
    ```
   This will:

    - Run unit and integration tests and generate report
    - Check tests coverage and generate report
    - Build application snapshot .jar

3. Build and start the containers:

   ```sh
   docker-compose up --build
   ```

   This will:

    - Start a MySQL container.
    - Build and start the Spring Boot application container.

4. The service API and Swagger documentation will be available at:

    - Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
    - API base URL: `http://localhost:8080`

### Stopping the Containers

To stop the running containers:

```sh
docker-compose down
```

## Running Tests

Tests are automatically executed before building the JAR file. However, you can manually run them with:

```sh
./gradlew test
```

## API Documentation

This project uses OpenAPI with Springdoc to generate API documentation.
After starting the application, visit:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Configuration

The application uses environment variables for database configuration when running inside Docker:

- `SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/mydatabase`
- `SPRING_DATASOURCE_USERNAME=***`
- `SPRING_DATASOURCE_PASSWORD=***`

## Directory Structure

```
├── src/main/java/com/example/devices    # Main application code
├── src/test/java/com/example/devices    # Test classes
├── Dockerfile                           # Docker build file
├── compose.yaml                         # Docker Compose configuration
├── build.gradle                         # Gradle build script
├── README.md                            # This file
```

## Troubleshooting

- If `docker-compose up --build` fails due to a port conflict, ensure no other services are running on port `3306` or `8080`.
- If MySQL does not initialize, try removing existing Docker volumes:
  ```sh
  docker-compose down -v
  ```
- If you need to rebuild the project, use:
  ```sh
  ./gradlew clean build
  ```