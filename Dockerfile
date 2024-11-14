<<<<<<< HEAD
FROM openjdk:17-jdk-alpine 
=======
FROM openjdk:17-jdk-alpine
>>>>>>> gestion_piste
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
<<<<<<< HEAD
=======

>>>>>>> gestion_piste
