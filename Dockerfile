# Sử dụng image Java từ Docker Hub
FROM openjdk:17-jdk-slim

# Thêm metadata
LABEL maintainer="your-email@example.com"

# Copy JAR file vào container
COPY target/backend-java-0.0.1-SNAPSHOT.jar /app/backend.jar

# Định nghĩa cổng mà ứng dụng sẽ chạy
EXPOSE 8080

# Chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
