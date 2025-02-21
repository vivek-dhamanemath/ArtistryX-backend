# Build stage: Using Maven to build the application
FROM maven:3-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the source code into the container
COPY . .

# Run Maven to clean and package the application (create a jar file)
RUN mvn clean package

# Runtime stage: Use a minimal base image with OpenJDK 17
FROM eclipse-temurin:17-alpine

# Set the working directory inside the runtime container
WORKDIR /app

# Copy the generated jar file from the build stage into the runtime container
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8081

# Command to run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
