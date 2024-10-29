package se.fk;

import static io.restassured.RestAssured.given;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
import java.util.Optional;

import jakarta.inject.Inject;

public class LoginResourceSteps {

    private Response response;
    private String email;


    @Inject
    RegisteredUsersRepository registeredUsersRepository;

    @Inject
    LoginResource loginResource;

//    @Given("The user with email {string} does not exist")
//    public void the_user_with_email_does_not_exist(String email) {
//        // Kontrollera att användaren inte redan finns via ett GET-anrop
//        response = given()
//                .when()
//                .get("/auth/requestaccess?email=" + email) // Antag att du har en metod för att kolla existens
//                .then()
//                .extract().response();
//
//        assertEquals(404, response.getStatusCode(), "User should not exist before registration"); // 404 betyder att användaren inte finns
//    }

    @Given("The user with email {string} does not exist")
    public void the_user_with_email_does_not_exist(String email) {
        // Hämta userId från e-postadressen
        Optional<RegisteredUsers> userOptional = registeredUsersRepository.findByEmail(email);

        // Om användaren inte finns, förväntar vi oss en 404-statuskod
        assertFalse(userOptional.isPresent(), "User should not exist before registration"); // Kontrollera att användaren inte finns
    }


    @When("I try to register a new user with name {string} and email {string}")
    public void i_try_to_register_a_new_user_with_name_and_email(String name, String email) {
        // Skapa en JSON-kropp för POST-anropet
        String userJson = "{ \"email\": \"" + email + "\" }"; // Använd endast e-post i registreringen

        // Skicka POST-anrop till /register
        response = given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/auth/register")
                .then()
                .extract().response();
    }

    @Then("The user is created")
    public void the_user_is_created() {
        // Verifiera att POST-anropet returnerar 201 (Created)
        assertEquals(201, response.getStatusCode(), "User should be created");
    }

    @Then("An error occurs with message \"Ogiltigt email-format\"")
    public void invalid_email_format() {
        // Kontrollera att statuskoden är 400 (Bad Request)
        assertEquals(400, response.getStatusCode(), "Expected status code 400 for invalid email");

        // Kontrollera att svaret innehåller felmeddelandet "Ogiltigt email-format"
        String errorMessage = response.jsonPath().getString("message"); // Förutsatt att felmeddelandet returneras i "message"-fältet
        assertEquals("Ogiltigt email-format", errorMessage, "Expected error message for invalid email");
    }

    @Then("An error occurs with status {int} and message {string}")
    public void verify_error_with_status_and_message(int expectedStatusCode, String expectedMessage) {
        // Kontrollera att statuskoden matchar den förväntade
        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, response.getStatusCode(), "Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        System.out.println("Actual Status Code: " + response.getStatusCode());
        // Kontrollera att svaret innehåller det förväntade felmeddelandet
        String actualMessage = response.jsonPath().getString("message");
        assertEquals(expectedMessage, actualMessage, "Expected error message: " + expectedMessage);
    }

    @Given("An user with email {string} is already registered")
    public void a_user_with_email_is_already_registered(String email) {
        // Kontrollera om användaren redan finns
        Optional<RegisteredUsers> existingUser = registeredUsersRepository.findByEmail(email);

        // Om användaren inte existerar, registrera den
        if (existingUser.isEmpty()) {
            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            loginResource.registerUser(request);  // Registrera användaren
        } else {
            System.out.println("Användaren med e-post " + email + " finns redan i databasen.");
        }
    }


}