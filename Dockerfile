FROM docker pull maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-alpine
# Copy the jar file from your local machine to the Docker container
COPY --from=build /target/*.jar app.jar
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
