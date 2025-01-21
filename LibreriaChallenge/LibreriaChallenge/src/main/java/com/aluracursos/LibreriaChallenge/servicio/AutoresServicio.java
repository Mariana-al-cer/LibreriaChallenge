package com.aluracursos.LibreriaChallenge.servicio;

import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeAutores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoresServicio {
    private final RepositorioDeAutores repositorioDeAutores;

    @Autowired
    public AutoresServicio(RepositorioDeAutores repositorioDeAutores) {
        this.repositorioDeAutores = repositorioDeAutores;
    }

}
