
package se.fk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); // skapar en ValidatorFactory
        validator = factory.getValidator(); // hämtar instans av Validator
    }

    @Test
    public void testValidEmail() {
        // Skapar en LoginRequest och sätter en giltig email-adress
        LoginRequest request = new LoginRequest();
        request.setEmail("valid.email@example.com");
        // Validerar objektet och lagra eventuella fel
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        // Kontrollerar att inga valideringsfel uppstod
        assertTrue(violations.isEmpty(), "Should not have violations for valid email");
    }

    @Test
    public void testEmailIsRequired() {
        // Skapar en LoginRequest och sätter email-adressen till null
        LoginRequest request = new LoginRequest();
        request.setEmail(""); // Saknad email ska generera valideringsfel i LoginRequest-klassen

        // Validering av LoginRequest-objektet.
        // validator.validate(request) Valideringsmetoden kontrollerar om objektet request uppfyller alla valideringar.
        // metoden returnerar en mängd (set) av ConstraintViolation<LoginRequest> innehållande alla
        // valideringsfel för objektet som validerades. Om inga fel finns är mängden tom.
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        // kontroll att mängden av violations inte är tom
        assertFalse(violations.isEmpty(), "Should have violations for empty email");
        // assertEquals("Email is required", violations.iterator().next().getMessage());
        // Kontrollera att ett specifikt felmeddelande finns i violations
        boolean hasRequiredMessage = violations.stream()
                .anyMatch(violation -> violation.getMessage().equals("Email is required"));
        assertTrue(hasRequiredMessage, "Should have violation with message 'Email is required'");
    }

    @Test
    public void testInvalidEmailFormat() {
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid-email-format"); // Invalid email format

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Should have violations for invalid email format");
        // assertEquals("Ogiltigt email-format", violations.iterator().next().getMessage());
        // Kontrollera att ett specifikt felmeddelande finns i violations
        boolean hasRequiredMessage = violations.stream()
                .anyMatch(violation -> violation.getMessage().equals("Ogiltigt email-format"));
        assertTrue(hasRequiredMessage, "Should have violation with message 'Email is required'");
    }

    @Test
    public void testEmailDoesNotMatchPattern() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@1"); // Invalid email according to pattern

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Should have violations for email not matching pattern");
        // assertEquals("Ogiltigt email-format", violations.iterator().next().getMessage());
        // Kontrollera att ett specifikt felmeddelande finns i violations
        boolean hasRequiredMessage = violations.stream()
                .anyMatch(violation -> violation.getMessage().equals("Ogiltigt email-format"));
        assertTrue(hasRequiredMessage, "Should have violation with message 'Email is required'");
    }
}
