FROM openjdk:17-jdk-slim
LABEL authors="Prateek Arya"
EXPOSE 9000
COPY target/cards-0.0.1-SNAPSHOT.jar cards-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","/cards-0.0.1-SNAPSHOT.jar"]