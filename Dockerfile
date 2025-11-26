# ----------------------------------------------------
# FASE 1: CONSTRUCCIÓN (BUILD PHASE)
# Usa Maven y JDK 21 para compilar el código.
# ----------------------------------------------------
FROM maven:3.9.5-amazoncorretto-21 AS build
WORKDIR /app
# Copia archivos de Maven y código fuente
COPY pom.xml .
COPY src /app/src
# Compila el proyecto Spring Boot y omite las pruebas
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# FASE 2: EJECUCIÓN (RUN PHASE)
# ¡CORRECCIÓN FINAL! Usamos amazoncorretto:21-jre-alpine (imagen ligera y estable)
# ----------------------------------------------------
FROM amazoncorretto:21-jre-alpine
WORKDIR /app
# Expone el puerto por defecto de Spring Boot
EXPOSE 8083
# Copia el JAR compilado desde la fase 'build'
COPY --from=build /app/target/Libreria-0.0.1-SNAPSHOT.jar app.jar
# Comando de inicio: ejecuta el JAR de tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]