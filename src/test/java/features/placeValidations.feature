Feature: Validating Place APIs

  @AddPlace @Regression
  Scenario Outline: Verify if place is being successfully added using AppPlaceAPI
    Given Add Place Payload with "<name>" "<language>" "<address>"
    When user calls "addPlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    And verify place_id created maps to "<name>" using "getPlaceAPI"
    Examples:
      | name    | language | address            |
      | AAhouse | English  | World cross center |
      | BBHouse | French   | Seaport Harbor     |

  @DeletePlace @Regression
  Scenario: Verify if delete place functionality is working
    Given DeletePlace Payload
    When user calls "deletePlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"

  Scenario: Call OAuth protected API
    Given I call OAuth server to obtain access token
    When I use the access token to call getCourseDetails API
    Then course instructor is "RahulShetty"
    Then get the price of course "SoapUI Webservices testing"
    And "webAutomation" courses include the following titles
      | Selenium Webdriver Java |
      | Cypress                 |
      | Protractor              |
