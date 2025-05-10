# STAGE 1: Building app with Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# STAGE 2: Executing with OpenJDK
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar kairos_app.jar
EXPOSE 8080 8443
ENTRYPOINT ["java", "-jar", "/app/kairos_app.jar"]
