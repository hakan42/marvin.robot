FROM openjdk:23-jdk-slim

WORKDIR /app

ADD target/marvin.interaction.web-1.0-SNAPSHOT.jar /app/marvin.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "marvin.jar"]