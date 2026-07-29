# 1. Usamos una imagen con Maven y Java 17 para compilar el proyecto
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos los archivos del proyecto al contenedor
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto creando el archivo .jar
RUN mvn clean package -DskipTests

# 2. Usamos una imagen ligera de Java 17 solo para ejecutar la app
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el .jar generado 
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Comando para arrancar el bot
ENTRYPOINT ["java", "-jar", "app.jar"]