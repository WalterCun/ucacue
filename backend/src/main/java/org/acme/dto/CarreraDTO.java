package org.acme.dto;

public class CarreraDTO {
    public Long id;
    public String nombre;
    public Integer numeroEstudiante;

    public CarreraDTO() {}

    public CarreraDTO(Long id, String nombre, Integer numeroEstudiante) {
        this.id = id;
        this.nombre = nombre;
        this.numeroEstudiante = numeroEstudiante;
    }

    @Override
    public String toString() {
        return "CarreraDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numeroEstudiante=" + numeroEstudiante +
                '}';
    }
}
