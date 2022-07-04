FROM openjdk:17.0.2

ARG JARFILE=build/libs/*.jar
COPY ${JARFILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
