FROM openjdk:17-jdk-slim

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build --info
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]