# Etapa 1: Compilar la aplicación con permisos de Administrador
FROM gradle:8.5-jdk17 AS build
USER root
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Forzamos los permisos de ejecución del compilador
RUN chmod +x /home/gradle/src/build.gradle.kts
RUN gradle build -x test --no-daemon

# Etapa 2: Imagen de ejecución ligera
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/plottwist-app.jar
ENTRYPOINT ["java", "-jar", "/app/plottwist-app.jar"]