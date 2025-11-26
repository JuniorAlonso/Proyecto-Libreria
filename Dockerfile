# ----------------------------------------------------
# FASE 1: CONSTRUCCIÓN (BUILD PHASE)
# Usa Maven y JDK 21 para compilar tu código y generar el JAR.
# ----------------------------------------------------
FROM maven:3.9.5-amazoncorretto-21 AS build
WORKDIR /app
# Copia los archivos de configuración y el código fuente
COPY pom.xml .
COPY src /app/src
# Compila el proyecto Spring Boot y omite las pruebas
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# FASE 2: EJECUCIÓN (RUN PHASE)
# Usa una imagen ligera (solo JRE 21) para ejecutar la aplicación final.
# ----------------------------------------------------
FROM openjdk:21-jdk-slim
WORKDIR /app
# Expone el puerto por defecto de Spring Boot
EXPOSE 8080
# Copia el JAR compilado desde la fase 'build'
COPY --from=build /app/target/Libreria-0.0.1-SNAPSHOT.jar app.jar
# Comando de inicio: ejecuta el JAR de tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]