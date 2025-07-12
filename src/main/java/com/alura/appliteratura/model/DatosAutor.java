package com.alura.appliteratura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") String fechaNac,
        @JsonAlias("death_year") String anioFallecimiento
) {

    @Override
    public String toString() {
        return "Autor: " + nombre +
                " (Nacimiento: " + fechaNac +
                ", Fallecimiento: " + anioFallecimiento + ")";
    }
}
