package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateInscripcionDTO;
import org.acme.model.Carrera;
import org.acme.model.Colegio;
import org.acme.model.Inscripcion;
import org.acme.model.User;
import org.acme.repository.InscripcionesRepository;
import jakarta.ws.rs.*;

import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class InscripcionesService {
    
    @Inject
    InscripcionesRepository inscripcionesRepository;
    
    /**
     * Crea un nuevo usuario
     */
    @Transactional
    public Inscripcion createInscripcion(@Valid Inscripcion ins) {
        // Ya debe venir con colegio y carrera asignados como entidades en el controlador
        if (ins.colegio == null) {
            throw new RuntimeException("El colegio es requerido");
        }
        if (ins.carrera == null) {
            throw new RuntimeException("La carrera es requerida");
        }

        // Persistimos la inscripción primero
        inscripcionesRepository.persist(ins);

        // Luego incrementamos el número de estudiantes de la carrera asociada
        Carrera carrera = ins.carrera;
        if (carrera != null) {
            // Asegurar no nulo y valor inicial 0 si fuera necesario
            if (carrera.numeroEstudiante == null) {
                carrera.numeroEstudiante = 0;
            }
            carrera.numeroEstudiante = carrera.numeroEstudiante + 1;
            // Como es una entidad administrada (viene de findById en el controller),
            // Hibernate detectará el cambio y hará update al hacer flush/commit.
            // Si por alguna razón no estuviera administrada, podemos forzar persist.
            Carrera.persist(carrera);
        }

        return ins;
    }


}
