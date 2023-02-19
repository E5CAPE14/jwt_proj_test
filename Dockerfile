FROM openjdk:19-alpine
COPY /target/P_project-0.0.1-SNAPSHOT.jar jwt_project.jar
ENTRYPOINT ["java","-jar","/jwt_project.jar"]