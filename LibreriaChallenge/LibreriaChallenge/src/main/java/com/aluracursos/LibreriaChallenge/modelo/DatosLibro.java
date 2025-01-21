package com.aluracursos.LibreriaChallenge.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro (
    @JsonAlias("id") Long id,
    @JsonAlias("title") String titulo,
    @JsonAlias("authors") List<DatosAutor> autor,
    @JsonAlias("languages") List<String> lenguajes,
    @JsonAlias("subjects") List<String> descripcionLibro
    ) {

    public DatosLibro {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro no puede ser nulo ni vacío.");
        }
    }
}
