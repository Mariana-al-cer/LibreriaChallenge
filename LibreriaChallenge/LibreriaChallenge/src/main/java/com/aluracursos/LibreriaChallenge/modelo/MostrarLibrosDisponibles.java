package com.aluracursos.LibreriaChallenge.modelo;

import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeLibros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MostrarLibrosDisponibles {

    @Autowired
    RepositorioDeLibros repositorioDeLibros;

    @Transactional
    private void mostrarLibrosDisponibles() {
        List<Libro> todosLosLibros = repositorioDeLibros.findAllWithAutores();}
}
