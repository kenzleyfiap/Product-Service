package br.com.kenzley.fiap.service.product.api;

import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import br.com.kenzley.fiap.service.product.utils.ProductHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() throws IOException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        mongoTemplate.dropCollection(ProductEntity.class);
        loadTestData();
    }

    private void loadTestData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeReference<List<ProductEntity>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = new ClassPathResource("data.json").getInputStream();
        List<ProductEntity> products = mapper.readValue(inputStream, typeReference);
        mongoTemplate.insertAll(products);
    }

    @Nested
    class RegisterProduct{

        @Test
        void mustAllowRegisterProduct() {

            var product = ProductHelper.gerarProductRequest();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(product)
                    .when()
                    .post("/product")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void mustGenerateExceptionWhenUpdateProductPayloadXML(){
            String xmlPayload = "<productRequestDTO><name>hamburguer</name></productRequestDTO>";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(xmlPayload)
                    .when()
                    .post("/product")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class FindProduct{
        @Test
        void mustAllowFindProduct() {
            var id = UUID.fromString("a0d94841-8ac4-4346-af6d-dbb82bd316cc");

            when()
                    .get("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
        @Test
        void mustGenerateExceptionWhenIdNotFound() {
            var id = "2e9333f3-0789-451d-96db-50374602fd2a";
            when()
                    .get("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class UpdateProduct{
        @Test
        void mustAllowUpdateProduct() {

            var id = UUID.fromString("a0d94841-8ac4-4346-af6d-dbb82bd316cc");

            var product = ProductHelper.gerarProductRequest();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(product)
                    .when()
                    .put("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
        @Test
        void mustGenerateExceptionWhenPayloadXML() {
            var id = UUID.fromString("780e2a14-d429-436f-a17e-f560e3929aa2");
            String xmlPayload = "<productRequestDTO><name>hamburguer</name></productRequestDTO>";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(xmlPayload)
                    .when()
                    .put("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
        @Test
        void mustGenerateExceptionWhenIdNotFound() {
            var id = UUID.fromString("780e2a14-d429-436f-a17e-f560e3929aa");

            var product = ProductHelper.gerarProductRequest();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(product)
                    .when()
                    .put("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

    }

    @Nested
    class RemoveProduct{

        @Test
        void mustAllowRemoveProduct() {
            var id = UUID.fromString("a0d94841-8ac4-4346-af6d-dbb82bd316cc");

            when()
                    .delete("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }
        @Test
        void mustGenerateExceptionWhenIdNotFound() {
            var id = UUID.fromString("b3543705-8008-42bc-84c0-e0c094076e2");

            when()
                    .delete("/product/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class ListProduct{
        @Test
        void mustAllowListProduct() {

            when()
                    .get("/product")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
    }

}
