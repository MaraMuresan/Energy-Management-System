# ==========================
# 1. BUILD STAGE
# ==========================
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /build

# Copy only pom.xml first (for caching dependencies)
COPY pom.xml .

# Copy the rest of the source code
COPY src ./src

# Build the JAR (skipping tests for faster build)
RUN mvn clean package -DskipTests

# ==========================
# 2. RUNTIME STAGE
# ==========================
FROM eclipse-temurin:21-jre

# Set working directory for runtime container
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
