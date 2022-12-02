FROM amazoncorretto:11-alpine
RUN mkdir "docker-image"
WORKDIR /server
ENTRYPOINT ["./gradlew", "clean", "bootjar", "-x", "test", "-x", "asciidoctor"]
ARG JAR_FILE=server/build/libs/*.jar
COPY ${JAR_FILE} /docker-image/app.jar
ENTRYPOINT ["java", "-jar", "/docker-image/app.jar"]