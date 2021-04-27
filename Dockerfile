FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=messaging-service/build/libs/messaging-service-1.0.jar
EXPOSE 8080
RUN apk add --no-cache bash
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
COPY lib/build/libs/lib-1.0.jar lib/lib.jar
CMD ["java","-Dspring.profiles.active=dev","-jar","app.jar"]