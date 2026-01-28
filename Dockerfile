# Dockerfile for Railway deployment
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml mvnw .mvn .mvn/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src/

# Build the application
RUN mvn clean package -DskipTests -B

# Final stage
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port (Railway uses PORT environment variable)
EXPOSE 8080

# Create a non-root user for security
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring
USER 1001:1001

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]