# 1. Usamos Maven con soporte para versiones de Java más recientes
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copiamos los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto creando el .jar
RUN mvn clean package -DskipTests

# 2. Usamos el ejecutable de Java 21/25 para arrancar
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copiamos el .jar compilado
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]