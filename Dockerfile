FROM amazoncorretto:11-alpine
#WORKDIR /server
#ENTRYPOINT ["./gradlew", "clean", "bootjar", "-x", "test", "-x", "asciidoctor"]
COPY ./server/build/libs/*.jar hitch-hiker-server.jar
ENTRYPOINT ["java", "-jar", "hitch-hiker-server.jar"]