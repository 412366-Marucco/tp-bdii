# Etapa 1: construir con Maven
FROM maven:3.8.5-openjdk-17 AS build

# Creamos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos todo el proyecto al contenedor
COPY . .

# Compilamos el proyecto y generamos el JAR
RUN mvn clean package -DskipTests

# Etapa 2: usar solo el JAR con una imagen más liviana
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos solo el .jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando de inicio del backend
ENTRYPOINT ["java", "-jar", "app.jar"]
