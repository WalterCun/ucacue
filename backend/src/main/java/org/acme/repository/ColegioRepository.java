package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Colegio;

import java.util.List;

@ApplicationScoped
public class ColegioRepository implements PanacheRepository<Colegio> {
    // Use PanacheRepository's built-in methods (listAll, find, count, etc.)

    public List<Colegio> all() {
        return listAll();
    }
}
