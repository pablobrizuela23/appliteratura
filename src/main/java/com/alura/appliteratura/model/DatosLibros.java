package com.alura.appliteratura.model;


import com.alura.appliteratura.principal.Principal;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
       @JsonAlias("title") String titulo,
       @JsonAlias("authors") List<DatosAutor> autores,
       @JsonAlias("languages") List<String> idiomas,
       @JsonAlias("download_count") Long numeroDescargas) {

    public DatosAutor getPrimerAutor() {
        return autores != null && !autores.isEmpty() ? autores.get(0) : null;
    }


}
