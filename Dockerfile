FROM adoptopenjdk/openjdk11
LABEL maintainer="evadcmd@gmail.com"
COPY build/libs/demo-0.0.1-SNAPSHOT.jar demo-app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/demo-app.jar"]