package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlaceRequest;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefinition extends Utils {
    public RequestSpecification reqSpec;
    public ResponseSpecification resSpec;
    public Response response;
    public TestDataBuild data = new TestDataBuild();
    public static String placeId;

    @Given("Add Place Payload with {string} {string} {string}")
    public void add_place_payload(String name, String language, String address) throws IOException {
        AddPlaceRequest addPlaceRequest = data.addPlacePayload(name, language, address);

        reqSpec = given()
                .spec(initialRequestSpec())
                .body(addPlaceRequest);
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_post_http_request(String resource, String httpMethod) {
        APIResources apiResources = APIResources.valueOf(resource);
        if (httpMethod.equalsIgnoreCase("POST")) {
            response = reqSpec.when().post(apiResources.getResource());
        } else if (httpMethod.equalsIgnoreCase("GET")) {
            response = reqSpec.when().get(apiResources.getResource());
        }
    }

    @Then("the API call got success with status code {int}")
    public void the_api_call_got_success_with_status_code(int code) {
        assertEquals(code, response.getStatusCode());
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String key, String value) {
        String returnedValue = getJsonPathValue(response, key);
        assertEquals(value, returnedValue);
    }

    @Then("verify place_id created maps to {string} using {string}")
    public void verify_place_id_created_maps_to_using(String name, String resource) throws IOException {
        placeId = getJsonPathValue(response, "place_id");
        reqSpec = given()
                .spec(initialRequestSpec())
                .queryParam("place_id", placeId);

        user_calls_with_post_http_request(resource, "GET");
        String returnedName = getJsonPathValue(response, "name");
        assertEquals(name, returnedName);
    }

    @Given("DeletePlace Payload")
    public void delete_place_payload() throws IOException {
        String deletePlaceRequest = data.deletePlacePayload(placeId);

        reqSpec = given()
                .spec(initialRequestSpec())
                .body(deletePlaceRequest);
    }
}
