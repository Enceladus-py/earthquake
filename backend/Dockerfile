FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y gradle
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test
ARG JAR_FILE=build/libs/backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]