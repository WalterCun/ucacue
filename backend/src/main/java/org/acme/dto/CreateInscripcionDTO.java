package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateInscripcionDTO {

    @NotBlank
    public String nombre;

    @NotBlank
    public String cedula;

    @NotBlank
    @Email
    public String email;

    @NotNull
    @JsonAlias({"colegio_id"})
    public Long colegioId;

    @NotNull
    @JsonAlias({"carrera_id"})
    public Long carreraId;

    // subtotal
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2)
    public BigDecimal subtotal;

    // descuento
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2)
    public BigDecimal descuento;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor a 0")
    @Digits(integer = 12, fraction = 2)
    public BigDecimal costo;
}
