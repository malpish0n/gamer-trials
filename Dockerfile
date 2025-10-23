FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src src

CMD ["./mvnw", "spring-boot:run"]
