FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/app

COPY webdav-gateway-0.0.1-SNAPSHOT.jar app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]