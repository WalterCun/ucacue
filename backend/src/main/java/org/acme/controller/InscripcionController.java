package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateInscripcionDTO;
import org.acme.model.Carrera;
import org.acme.model.Colegio;
import org.acme.model.Inscripcion;
import org.acme.service.InscripcionesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/inscripciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InscripcionController {

    @Inject
    InscripcionesService inscripcionesService;

    @GET
    public List<Inscripcion> listAll() {
        return Inscripcion.listAll();
    }

    @POST
    public Response create(@Valid CreateInscripcionDTO dto) {
        try {
            if (dto == null) {
                return error(Response.Status.BAD_REQUEST, "Los datos de la inscripción son requeridos");
            }

            Colegio colegio = Colegio.findById(dto.colegioId);
            if (colegio == null) {
                return error(Response.Status.BAD_REQUEST, "El colegio especificado no existe");
            }
            Carrera carrera = Carrera.findById(dto.carreraId);
            if (carrera == null) {
                return error(Response.Status.BAD_REQUEST, "La carrera especificada no existe");
            }

            Inscripcion ins = new Inscripcion();
            ins.nombre = dto.nombre;
            ins.cedula = dto.cedula;
            ins.email = dto.email;
            ins.colegio = colegio;
            ins.carrera = carrera;
            ins.subtotal = dto.subtotal;
            ins.descuento = dto.descuento;
            ins.costo = dto.costo;

            Inscripcion insCreate = inscripcionesService.createInscripcion(ins);

            return Response.status(Response.Status.CREATED).entity(insCreate).build();
        } catch (RuntimeException e) {
            return error(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return error(Response.Status.INTERNAL_SERVER_ERROR, "Error interno al crear la inscripción");
        }
    }

    private Response error(Response.Status status, String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("error", message);
        return Response.status(status).entity(payload).build();
    }
}
