package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "carreras")
public class Carrera extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    public String nombre;

    @NotNull
    @Column(name = "numero_estudiante", nullable = false)
    public Integer numeroEstudiante;
}
