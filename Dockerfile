FROM openjdk:11

COPY ./target/backend-api-0.0.1-SNAPSHOT.jar backend-api-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java","-jar","backend-api-0.0.1-SNAPSHOT.jar"]