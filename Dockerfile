FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/target/proyecto-saber-pro-0.0.1-SNAPSHOT.jar"]