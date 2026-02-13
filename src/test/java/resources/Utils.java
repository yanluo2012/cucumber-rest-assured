package resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

/**
 * Base class for step definitions
 * Creates and caches a singleton RequestSepcification object
 * Reads configurtion from global.properties file
 * Extracts values from API responses using JsonPath
 */
public class Utils {
    public static RequestSpecification initReqSpec;
    public RequestSpecification initialRequestSpec() throws IOException {
        if (initReqSpec == null) {
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
            initReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalValue("baseUrl"))
                    .addQueryParam("key", "qaclick123")
                    .setContentType(ContentType.JSON)
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .build();
        }
        return initReqSpec;
    }

    /**
     * @param key global property name
     * @return global property value
     * @throws IOException
     */
    public String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("/Users/yan.lai/workspace_atlas_v4/cucumber_rest_assured/src/test/java/resources/global.properties");
        prop.load(fis);
        return prop.get(key).toString();
    }

    /**
     * @param response API response
     * @param key property of the response
     * @return value of the property in the response
     */
    public String getJsonPathValue(Response response, String key) {
        JsonPath jp = new JsonPath(response.asString());
        return jp.getString(key).toString();
    }
}
