package com.aluracursos.LibreriaChallenge.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosAutor(
        @JsonAlias("idAutor") String idAutor,
    @JsonAlias("name") String nombreAutor,
    @JsonAlias("birth_year") Integer fechaDeNacimiento,
    @JsonAlias("death_year") Integer fechaDeMuerte
    ) {}
