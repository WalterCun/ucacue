package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "colegios")
public class Colegio extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

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
