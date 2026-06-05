# Etapa 1: Compilar la aplicación
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/plottwist-app.jar
ENTRYPOINT ["java", "-jar", "/app/plottwist-app.jar"]