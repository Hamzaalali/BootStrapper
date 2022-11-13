FROM openjdk:18
COPY target/bootstrapper.jar bootstrapper.jar

ENTRYPOINT ["java","-jar","/bootstrapper.jar"]

