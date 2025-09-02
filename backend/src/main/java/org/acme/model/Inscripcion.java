package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "inscripciones")
public class Inscripcion extends PanacheEntity {

    @NotBlank
    @Column(name = "nombre", nullable = false)
    public String nombre;

    @NotBlank
    @Column(name = "cedula", nullable = false)
    public String cedula;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false)
    public String email;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id", nullable = false)
    public Colegio colegio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    public Carrera carrera;

    // subtotal de la inscripción
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2)
    @Column(name = "subtotal", nullable = false)
    public BigDecimal subtotal;

    // descuento aplicado a la inscripción
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2)
    @Column(name = "descuento", nullable = false)
    public BigDecimal descuento;


    // Conservamos el costo referencial existente si fuera usado en otros lugares
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor a 0")
    @Digits(integer = 12, fraction = 2)
    @Column(name = "costo", nullable = false)
    public BigDecimal costo = new BigDecimal("100.00");
}
