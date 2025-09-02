package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Inscripcion;
import org.acme.model.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InscripcionesRepository implements PanacheRepository<Inscripcion> {
}
