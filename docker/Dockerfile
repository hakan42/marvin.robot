FROM openjdk:23-jdk-slim

WORKDIR /app

ADD ../marvin.interaction.web/target/marvin.interaction.web-1.0-SNAPSHOT.jar /app/marvin.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "marvin.jar"]