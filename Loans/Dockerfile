FROM openjdk:17-jdk-slim
LABEL authors="Prateek Arya"
EXPOSE 8081
COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","/loans-0.0.1-SNAPSHOT.jar"]