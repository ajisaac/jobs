FROM openjdk:17-jdk-alpine
COPY target/scraper-frontend-0.0.1.jar scraper-frontend-0.0.1.jar
ENTRYPOINT ["java","-jar","/scraper-frontend-0.0.1.jar"]