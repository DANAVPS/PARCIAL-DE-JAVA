FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar solo los archivos necesarios para cachear dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/target/proyecto-saber-pro-0.0.1-SNAPSHOT.jar"]