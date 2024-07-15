FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/product-1.0.0.jar product-1.0.0.jar

EXPOSE 8081
CMD ["java","-jar","product-1.0.0.jar"]