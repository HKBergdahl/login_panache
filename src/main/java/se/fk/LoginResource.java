package se.fk;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Base64;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.ConstraintViolationException;


//import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

@Path("/auth") //CG
public class LoginResource {

    // Dependencyinjections för
    @Inject // registrerade användare
            RegisteredUsersRepository registeredUsersRepository;

    @Inject // inloggningsförsök
    LoginAttemptRepository loginAttemptRepository; // Säkerställ att detta finns

    // Skapa en logger-instans för att registrera händelser i systemet
    private static final Logger logger = LoggerFactory.getLogger(LoginResource.class);

    // Datastruktur för att lagra login-förfrågningar temporärt
    private Map<String, String> tokens = new ConcurrentHashMap<>();

    // POST-bgäran för inloggningar
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON) //
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login with email", description = "Kontrollerar om email-adressen är godkänd.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Access granted - inloggningen lyckades"),
            @APIResponse(responseCode = "400", description = "Ogiltig förfrågan - felaktig email eller validering misslyckades"),
            @APIResponse(responseCode = "404", description = "Email inte funnen"),
            @APIResponse(responseCode = "500", description = "Serverfel - ett oväntat fel inträffade")
    })
    // Tar emot en validerad begäran
    public Response login(@Valid LoginRequest request) {
        String email = request.getEmail(); // Hämtar epostadressen från inloggningsbegärnan
        logger.info("Attempting login for email: {}", email); // Loggar inloggningsförsöket

        // Kontrollera om e-posten är registrerad i databasen
        if (registeredUsersRepository.findByEmail(email).isEmpty()) {
            logger.warn("Email not found: {}", email); // Loggar varning om att epost inte finns
            return buildJsonResponse(Response.Status.NOT_FOUND, "Email not found");
        }

        // Skapa och lagra en ny login-förfrågan
        LoginAttempt attempt = new LoginAttempt(email);
        loginAttemptRepository.save(attempt); // Sparar inloggningsförsöket i databasen

        // Generera token och koppla till användaren
        String token = generateToken();
        tokens.put(email, token); // Koppla token till användarens epost
        logger.info("Generated login link: /auth/requestaccess?token={}", token);

        return buildJsonResponse(Response.Status.OK, "Email sent"); // Returnerar lyckad inloggning
    }


    // GET-begäran för att verifiera token
    @GET
    @Path("/requestaccess")
    public Response requestAccess(@QueryParam("token") String token) {
        // Validera om tokenen finns i tokens-map:en
        if (tokens.containsValue(token)) {
            logger.info("Access granted for token: {}", token); // Logga access granted
            return Response.ok().entity("{\"message\": \"Access granted\"}").build(); // Returnera access granted
        } else {
            logger.warn("Access denied for token: {}", token); // Logga access denied
            return buildJsonResponse(Response.Status.FORBIDDEN, "Access denied"); // Returnera access denied
        }
    }

    // Hjälpmetod för att bygga JSON-svar
    private Response buildJsonResponse(Response.Status status, String message) {
        String jsonMessage = String.format("{\"message\": \"%s\"}", message);
        return Response.status(status).entity(jsonMessage).build();
    }


    // Generera en token baserat på epost och nuvarande tid
    private String generateToken() {
        return UUID.randomUUID().toString();
    }


    // POST för att registrera användare
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid LoginRequest request) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // Hämtar epostadressen från registreringsbegäran
            String email = request.getEmail();
            logger.info("Registering email: {}", email); // Loggar registreringsförsöket
            System.out.println("Received email: " + email);  // Utskrift för att se vilken e-post som skickades in



            // Kontrollera om e-posten redan är registrerad
            if (registeredUsersRepository.findByEmail(email).isPresent()) {
                // Om epostadressen redan är registreras returneras en konflikt (409)
                return buildJsonResponse(Response.Status.CONFLICT, "Email already registered");
            }

            // Registrera användaren
            RegisteredUsers newUser = new RegisteredUsers(email);
            registeredUsersRepository.registerUser(newUser); // Lägg till användaren i databasen

            // Returnera en skapad respons (201) med meddelande om att registeringen lyckats
            return buildJsonResponse(Response.Status.CREATED, "User registered");

    }

}



class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Ogiltigt email-format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Ogiltigt email-format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}