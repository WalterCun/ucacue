package org.acme.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Carrera;

import java.util.List;

@Path("/carreras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarreraController {

    @GET
    public Response getAll() {
        List<Carrera> carreras = Carrera.listAll();

        return Response.ok(carreras).build();
    }
}
