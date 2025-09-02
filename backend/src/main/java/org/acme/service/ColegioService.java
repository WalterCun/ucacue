package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Colegio;
import org.acme.repository.ColegioRepository;

import java.util.List;

@ApplicationScoped
public class ColegioService {
    @Inject
    ColegioRepository colegioRepository;

    public List<Colegio> getAll() {
        List<Colegio> colegios = colegioRepository.all();
        if (colegios == null) {
            return java.util.Collections.emptyList();
        }
        return colegios.stream().filter(java.util.Objects::nonNull).toList();
    }
}
