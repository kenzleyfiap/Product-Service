package br.com.kenzley.fiap.service.product.bdd;

import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.utils.ProductHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinition {

    private Response response;
    private ProductResponseDTO productResponseDTO;
    private final String ENDPOINT_API_PRODUCT = "http://localhost:8080/product";

    @When("register a new product")
    public ProductResponseDTO register_a_new_product() {
        var productRequest = ProductHelper.gerarProductRequest();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post(ENDPOINT_API_PRODUCT);

        return response.then().extract().as(ProductResponseDTO.class);

    }

    @Then("the product is registered successfully")
    public void the_product_is_registered_successfully() {
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Then("must be presented")
    public void must_be_presented() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schemas/product.schema.json"));
    }

    @Given("that a product has already been published")
    public void that_a_product_has_already_been_published() {
         productResponseDTO = register_a_new_product();
    }

    @When("search for the message")
    public void search_for_the_message() {
       response = when()
                 .get(ENDPOINT_API_PRODUCT + "/{id}", productResponseDTO.id());
    }

    @Then("the product is displayed successfully")
    public void the_product_is_displayed_successfully() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schemas/product.schema.json"));
    }

    @When("make request to change message")
    public void make_request_to_change_message() {
        response = given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productResponseDTO)
                .when()
                    .put(ENDPOINT_API_PRODUCT + "/{id}", productResponseDTO.id());
    }
    @Then("the product is successfully shown")
    public void the_product_is_successfully_shown() {
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @When("request product removal")
    public void request_product_removal() {
        response = when()
                .delete(ENDPOINT_API_PRODUCT + "/{id}", productResponseDTO.id());
    }
    @Then("the product is removed successfully")
    public void the_product_is_removed_successfully() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
