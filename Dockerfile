FROM openjdk:11

COPY ./target/backend-api-0.0.1-SNAPSHOT.jar backend-api-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","backend-api-0.0.1-SNAPSHOT.jar"]