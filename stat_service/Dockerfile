FROM maven:3.8.6-jdk-11-slim AS build
COPY main_service/src /home/app/main_service/src
COPY stat_service/src /home/app/stat_service/src
COPY main_service/pom.xml /home/app/main_service
COPY stat_service/pom.xml /home/app/stat_service
COPY pom.xml /home/app
COPY checkstyle.xml /home/app
COPY lombok.config /home/app
RUN mvn -f /home/app/pom.xml clean install -Dmaven.test.skip

FROM openjdk:11
RUN mkdir /opt/results
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/app/stat_service/target/*.jar /app/stat_service.jar

ENTRYPOINT ["java", "-jar", "/app/stat_service.jar"]