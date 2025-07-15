# Multi-stage build for Kotlin + Gradle
FROM gradle:8-jdk21 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle files first (for better caching)
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle/ gradle/

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN gradle clean shadowJar --no-daemon -x test

# Runtime stage
FROM amazoncorretto:21-alpine

# Create non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -G appuser -u 1001

# Set working directory
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app
USER appuser

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
