Feature: Validating OAuth APIs

  Scenario: Call OAuth protected API
    Given I call OAuth server to obtain access token
    When I use the access token to call getCourseDetails API
    Then course instructor is "RahulShetty"
    Then get the price of course "SoapUI Webservices testing"
    And "webAutomation" courses include the following titles
      | Selenium Webdriver Java |
      | Cypress                 |
      | Protractor              |
