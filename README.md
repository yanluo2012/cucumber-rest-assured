# cucumber-rest-assured
Use JsonPath to inteprate response
Client Credentials Grant (machine-to-machine) for OAuth 2.0
• App sends client_id + client_secret
• Authorization server returns an access token
• App uses the token to call APIs
use org.apache.poi plugin to read test data from Excel file



# API Automation Framework (Cucumber + Rest Assured)

This project is an API automation framework designed to demonstrate end-to-end (E2E) workflow testing. It integrates **Cucumber** for BDD, **Rest Assured** for API interactions, and **JUnit 5** as the test engine.

## Tech Stack
* **Language:** Java
* **BDD Framework:** Cucumber 7.x
* **API Client:** Rest Assured
* **Unit Testing:** JUnit 5
* **Data Handling:** Apache POI (Excel)
* **Build Tool:** Maven

---

## Framework Architecture

The framework follows a modular structure to ensure maintainability and separation of concerns:



* **Features:** Gherkin files defining high-level API scenarios.
* **Step Definitions:** Mapping Gherkin steps to Java logic.
* **Utils:** Support for OAuth 2.0 token generation and Excel data parsing.
* **Payloads:** Managed via POJOs or Map-based templates.

---

## OAuth 2.0 Integration (Client Credentials)

The framework is configured to handle secure APIs using the **Client Credentials Grant Type**.

**The Workflow:**
1.  The `OAuthUtils` class reads `clientId` and `clientSecret` from `config.properties`.
2.  A pre-test hook fetches a fresh `access_token` from the authorization server.
3.  The token is automatically injected into the Request Specification header:  
    `Authorization: Bearer <token>`

---

## Data-Driven Testing with Apache POI

Instead of hardcoding values, this project uses **Excel (.xlsx)** as a data source.

* **Utility:** `ExcelReader.java` parses rows into a `List<Map<String, String>>`.
* **Usage:** You can specify the sheet name and row ID within the Cucumber `Examples` table to drive dynamic test execution.

---