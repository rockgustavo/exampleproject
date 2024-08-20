FROM maven:3.8.8 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build ./build/target/*.jar ./application.jar
EXPOSE 8081

ENTRYPOINT java -jar application.jar