FROM maven:3.8.5-openjdk-17 as build
WORKDIR /build

COPY . .

RUN mvn clean package

# Final app
FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]