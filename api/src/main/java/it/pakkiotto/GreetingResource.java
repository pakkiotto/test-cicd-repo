package it.pakkiotto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Path("/api")
@Slf4j
@RequiredArgsConstructor
public class GreetingResource {

    private final ObjectMapper objectMapper;

    @Path("get")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        log.info("Received");
        return "Hello from Quarkus REST";
    }


    @Path("post")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logRequest(@Context HttpHeaders httpHeaders, Map<String, Object> body) {
        try {
            // Preparare una mappa per il log
            Map<String, Object> logData = new HashMap<>();
            logData.put("headers", httpHeaders.getRequestHeaders());
            logData.put("body", body);

            // Convertire la mappa in JSON
            String logJson = objectMapper.writeValueAsString(logData);

            // Log unico con headers e body
            log.info("Ricevuta chiamata: {}", logJson);

            // Risposta
            return Response.ok(Map.of("status", "success", "message", "Dati loggati con successo")).build();

        } catch (Exception e) {
            log.error("Errore durante la generazione del log JSON", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Errore durante il logging")).build();
        }
    }
}
