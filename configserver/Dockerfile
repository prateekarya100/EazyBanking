FROM openjdk:17-jdk-slim
LABEL authors="Prateek Arya"
EXPOSE 8071
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","/configserver-0.0.1-SNAPSHOT.jar"]