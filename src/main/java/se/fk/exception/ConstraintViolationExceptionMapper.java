package se.fk.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider  // Gör denna klass till en JAX-RS provider för att fånga undantag
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Här kan du definiera vad som ska skickas tillbaka vid valideringsfel
        String message = "Ogiltigt email-format";  // Specifik felmeddelande för ogiltig email

        // Returnera ett 400 BAD REQUEST med felmeddelandet som JSON
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"" + message + "\"}")
                .build();
    }
}