FROM maven:3.9.9-ibm-semeru-23-jammy as build
WORKDIR /app
COPY ./desafio/pom.xml .
RUN mvn dependency:go-offline
COPY ./desafio/src ./src
RUN mvn package -DskipTests=true

FROM openjdk:25-slim
COPY --from=build /app/target/desafio-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]