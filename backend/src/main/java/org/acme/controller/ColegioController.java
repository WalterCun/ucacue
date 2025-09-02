package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ColegioDTO;
import org.acme.model.Colegio;
import org.acme.service.ColegioService;

import java.util.List;

@Path("/colegios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ColegioController {

    @Inject
    ColegioService colegioService;

    @GET
    public Response getAll() {
        List<Colegio> colegios = colegioService.getAll();
        return Response.ok(colegios).build();
    }
}
