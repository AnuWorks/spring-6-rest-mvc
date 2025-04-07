# Stage 1: Build the application
FROM gradle:jdk21-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and project files first to optimize build caching
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle settings.gradle ./
COPY . .

# Build the app (skip tests)
RUN ./gradlew clean build



# Stage 2: Create minimal runtime image
FROM gcr.io/distroless/java21-debian12

COPY --from=build /app/build/libs/spring-6-rest-mvc-0.0.1-SNAPSHOT.jar app.jar
#COPY --from=build /app/compose.yaml .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
