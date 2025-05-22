# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy everything and build the project
COPY . .
RUN mvn clean package -DskipTests

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre-noble
LABEL maintainer="jacopo.mattei@edu.unifi.it"

ENV TZ=Europe/Rome
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get upgrade -y && apt-get clean

RUN adduser --disabled-login spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
