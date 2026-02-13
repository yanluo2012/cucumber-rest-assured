package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlaceRequest;
import pojo.GetCourse;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepDefinition extends Utils {
    public RequestSpecification reqSpec;
    public ResponseSpecification resSpec;
    public Response response;
    public TestDataBuild data = new TestDataBuild();
    public static String placeId;
    public String accessToken;
    public GetCourse courseResponse;

    @Given("Add Place Payload with {string} {string} {string}")
    public void add_place_payload(String name, String language, String address) throws IOException {
        AddPlaceRequest addPlaceRequest = data.addPlacePayload(name, language, address);

        reqSpec = given().log().all()
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
        response.then().log().all();
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
        reqSpec = given().log().all()
                .spec(initialRequestSpec())
                .queryParam("place_id", placeId);

        user_calls_with_post_http_request(resource, "GET");
        String returnedName = getJsonPathValue(response, "name");
        assertEquals(name, returnedName);
    }

    @Given("DeletePlace Payload")
    public void delete_place_payload() throws IOException {
        String deletePlaceRequest = data.deletePlacePayload(placeId);

        reqSpec = given().log().all()
                .spec(initialRequestSpec())
                .body(deletePlaceRequest);
    }

    @Given("I call OAuth server to obtain access token")
    public void iCallOAuthServerToObtainAccessToken() {
        RestAssured.baseURI = "https://rahulshettyacademy.com/";

        String oAuthResponse = given().log().all()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when().post("oauthapi/oauth2/resourceOwner/token")
                .then().log().all()
                .assertThat().statusCode(200).extract().asString();

        JsonPath jp = new JsonPath(oAuthResponse);
        this.accessToken = jp.getString("access_token");
        System.out.println("access_token is " + this.accessToken);
    }

    @When("I use the access token to call getCourseDetails API")
    public void i_use_the_access_token_to_call_get_course_details_api() {
        this.courseResponse = given().log().all()
                .pathParam("accessToken", this.accessToken)
                .get("oauthapi/getCourseDetails?access_token={accessToken}")
                .then().log().all().extract().response().as(GetCourse.class);
        // I got the response body, but response code is 401, is this a bug?
    }

    @Then("course instructor is {string}")
    public void courseInstructorIs(String instructorName) {
        System.out.println("Course instructor is " + this.courseResponse.instructor);
    }

    @Then("get the price of course {string}")
    public void getThePriceOfCourse(String courseName) {
        Set<String> keySet = this.courseResponse.courses.keySet();
        for (String key : keySet) {
            List<GetCourse.Course> courses = this.courseResponse.courses.get(key);
            String price = courses.stream()
                    .filter(c -> c.courseTitle.equals(courseName))
                    .findFirst()
                    .map(c -> c.price)
                    .orElse(null);
            if (price != null) {
                System.out.println("price of SoapUI is " + price);
                break;
            }
        }
    }

    @And("{string} courses include the following titles")
    public void coursesIncludeTheFollowingTitles(String courseName, List<String> expectedTitles) {
        List<String> returnedTitles = courseResponse.courses.get(courseName).stream()
                .map(c -> c.courseTitle)
                .collect(Collectors.toList());
        System.out.println(returnedTitles);
        assertTrue(returnedTitles.equals(expectedTitles));
    }

    @Given("I need data of test case {string} in sheet {string} of file {string}")
    public void iNeedDataOfTestCaseInSheetOfFile(String testcaseName, String sheetName, String fileName) throws IOException {
        ExcelDriven ed = new ExcelDriven();
        List<String> values = ed.getData(testcaseName, sheetName, fileName);
    }
}
