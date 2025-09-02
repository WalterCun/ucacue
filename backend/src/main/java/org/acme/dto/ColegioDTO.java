package org.acme.dto;

public class ColegioDTO {
    public Long id;
    public String nombre;
    public String ciudad;
    public String tipo;

    public ColegioDTO() {}

    public ColegioDTO(Long id, String nombre, String ciudad, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "ColegioDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
