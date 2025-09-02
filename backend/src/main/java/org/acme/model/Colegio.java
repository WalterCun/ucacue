package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "colegios")
public class Colegio extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    public String nombre;

    @NotBlank
    @Column(nullable = false)
    public String ciudad;

    @NotBlank
    @Column(nullable = false)
    public String tipo;
}
