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


//import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

@Path("/auth") //CG
public class LoginResource {

    // Skapa en logger-instans för att registrera händelser i systemet
    private static final Logger logger = LoggerFactory.getLogger(LoginResource.class);

    // Datastruktur för att lagra login-förfrågningar
    private Map<String, String> tokens = new ConcurrentHashMap<>();

    // Lista för att lagra "tillåtna" emailadresser
    private List<String> allowedEmails;


    // Konstruktor för att initialisera listan med email-adresser
    public LoginResource() {
        allowedEmails = new ArrayList<>();
        allowedEmails.add("karin@karin.com");
        allowedEmails.add("hej@hej.com");
        allowedEmails.add("ett@ett.com");
    }



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
        String email = request.getEmail(); // Hämtar epostadressen från begäran
        logger.info("Attempting login for email: {}", email); // Loggar händelsen

            // Kontroll om epostadressen finns i listan över giltiga adresser
            if (allowedEmails.contains(email.toLowerCase())) { // Adressen är giltig
                String token = generateToken(); // Skapar en unik token
                tokens.put(email, token); // Kopplar token till användarens e-post
                logger.info("Generated login link: /auth/requestaccess?token={}", token); // Loggar genererad länk
                // Simulera att mail skickas
                return buildJsonResponse(Response.Status.OK, "Email sent"); // Skickar svar med status 200
            }
            else { // Eposten finns inte i listan över tillåtna adresser
                logger.warn("Email not found: {}", email); // Loggar varning
                return buildJsonResponse(Response.Status.NOT_FOUND, "Email not found"); // Skickar 404
            }
   }

    @GET
    @Path("/requestaccess")
    // Hanterar en GET-begäran för att verifiera token
    public Response requestAccess(@QueryParam("token") String token) {
        // Validera om tokenen finns i tokens-map:en
        if (tokens.containsValue(token)) {
            logger.info("Access granted for token: {}", token); // Logga access granted
            return Response.ok().entity("{\"message\": \"Access granted\"}").build(); // Logga access granted
        } else {
            logger.warn("Access denied for token: {}", token); // Logga access denied
            return buildJsonResponse(Response.Status.FORBIDDEN, "Access denied"); // Logga access denied
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

}



class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Ogiltigt email-format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}