FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# FROM openjdk:11
# WORKDIR /RecieptProcessor
# CMD [ "./gradlew", "clean", "bootJar" ]
# COPY build/libs/*.jar app.jar
EXPOSE 8080