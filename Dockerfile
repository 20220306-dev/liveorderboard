FROM openjdk:17-alpine

ARG JAR_FILE

ENV WORKING_DIRECTORY = 'opt/app'

WORKDIR $WORKING_DIRECTORY

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
