package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acme.model.Carrera;
import org.acme.model.Colegio;
import org.acme.model.Inscripcion;
import org.acme.repository.InscripcionesRepository;

@ApplicationScoped
public class InscripcionesService {
    
    @Inject
    InscripcionesRepository inscripcionesRepository;
    
    /**
     * Crea una nueva inscripción
     */
    @Transactional
    public Inscripcion createInscripcion(@Valid Inscripcion ins) {
        // Validación básica
        if (ins.colegio == null || ins.colegio.id == null) {
            throw new RuntimeException("El colegio es requerido");
        }
        if (ins.carrera == null || ins.carrera.id == null) {
            throw new RuntimeException("La carrera es requerida");
        }

        // Validación de cédula requerida
        if (ins.cedula == null || ins.cedula.isBlank()) {
            throw new RuntimeException("La cédula es requerida");
        }

        // Re-obtener entidades gestionadas dentro de esta transacción
        Colegio managedColegio = Colegio.findById(ins.colegio.id);
        if (managedColegio == null) {
            throw new RuntimeException("El colegio especificado no existe");
        }
        Carrera managedCarrera = Carrera.findById(ins.carrera.id);
        if (managedCarrera == null) {
            throw new RuntimeException("La carrera especificada no existe");
        }

        // Asignar las referencias gestionadas a la inscripción
        ins.colegio = managedColegio;
        ins.carrera = managedCarrera;

        // Persistir la inscripción
        inscripcionesRepository.persist(ins);

        // Incrementar el contador en la entidad gestionada (no llamar persist sobre ella)
        if (managedCarrera.numeroEstudiante == null) {
            managedCarrera.numeroEstudiante = 0;
        }
        managedCarrera.numeroEstudiante = managedCarrera.numeroEstudiante + 1;
        // No se llama a persist/merge en managedCarrera; el cambio se sincroniza al commit

        return ins;
    }
}
