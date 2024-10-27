FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jdk-ubi9-minimal
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
