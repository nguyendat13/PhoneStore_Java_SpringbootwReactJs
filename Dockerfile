# Stage 1: Build JAR bằng Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Chạy JAR với JDK
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/backend-java-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]
