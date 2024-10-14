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


//import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

@Path("/auth") //CG
public class LoginResource {


    // Skapar upp en lista med "tillåtna" emailadresser som kontrolleras mot
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
    public Response login(@Valid LoginRequest request) {
        // Kontroll om den inskickade mailadressen finns med i listan över godkända adreser.
        if (allowedEmails.contains(request.getEmail().toLowerCase())){
            return Response.ok("{\"message\":\"access granted\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"access denied\"}")
                    .build();
        }
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