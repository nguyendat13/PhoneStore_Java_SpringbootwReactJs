# Sử dụng OpenJDK 17
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR từ thư mục target
COPY target/*.jar app.jar

# Mở port cho Spring Boot
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]
