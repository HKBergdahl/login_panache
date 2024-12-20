package se.fk;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.validation.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;



import static org.junit.jupiter.api.Assertions.assertFalse;



@QuarkusTest
public class LoginResourceTest {



    private LoginResource loginResource;
    private RegisteredUsersRepository registeredUsersRepository;
    private Validator validator; // Instansvariabel för validator

    @BeforeEach
    public void setUp() {
        registeredUsersRepository = Mockito.mock(RegisteredUsersRepository.class);
        loginResource = new LoginResource();
        loginResource.registeredUsersRepository = registeredUsersRepository; // Injekt mocken
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRegisterUser_newUser() {
        // Skapa en giltig LoginRequest
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        // Mocka beteendet för findByEmail för att returnera en tom Optional
        Mockito.when(registeredUsersRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        Response response = loginResource.registerUser(request); // Anropa registerUser-metoden
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus()); // Verifiera att svaret är 201 CREATED

        // Kontrollera att registerUser-metoden anropas med korrekt parameter
        Mockito.verify(registeredUsersRepository).registerUser(Mockito.any(RegisteredUsers.class));

    }

    @Test
    void testRegisterUser_existingUser() {
        // Skapa en giltig LoginRequest
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");

        // Mocka beteendet för findByEmail för att returnera en tom Optional
        RegisteredUsers existingUser = new RegisteredUsers("test@example.com");
        Mockito.when(registeredUsersRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));

        Response response = loginResource.registerUser(request); // Anropa registerUser-metoden
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus()); // Verifiera att svaret är 409 CONFLICT

        // Kontrollera att registerUser-metoden aldrig anropas
        Mockito.verify(registeredUsersRepository, never()).registerUser(Mockito.any(RegisteredUsers.class));

    }

    @Test
    void testRegisteruser_validEmail_returnCreated() {
        LoginRequest request = new LoginRequest();
        request.setEmail("valid.email@example.com");
        Response response = loginResource.registerUser(request);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }




    @Test
    void testInvalidEmailShouldThrowConstraintViolation() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ogiltig-email"); // Använd en ogiltig e-postadress

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty()); // Kontrollera att det finns valideringsfel
    }


    @Test
    void testRegisterUser_invalidEmail_returnBadRequest_alt2() {
        // Skapa en LoginRequest med en ogiltig e-postadress
        LoginRequest request = new LoginRequest();
        request.setEmail("ogiltig-email"); // Använd en ogiltig e-postadress

        // Simulera en POST-begäran till register-anropet
        given()
                .contentType("application/json")
                .body(request) // Konvertera objektet till JSON
                .when()
                .post("/auth/register") // Kontrollera att sökvägen stämmer överens med din registerUser-metod
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode()); // Kontrollera att svaret är 400 Bad Request
    }
}