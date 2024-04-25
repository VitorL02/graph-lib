FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y


RUN apt-get install maven -y
VOLUME /temp
VOLUME /result
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /temp
VOLUME /result
COPY . .

EXPOSE 8080

COPY --from=build /target/lib.vitorLucasCrispim-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]