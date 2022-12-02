FROM amazoncorretto:11-alpine
WORKDIR /server
ENTRYPOINT ["./gradlew", "clean", "bootjar", "-x", "test", "-x", "asciidoctor"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]