package com.aluracursos.LibreriaChallenge.servicio;

import com.aluracursos.LibreriaChallenge.modelo.Libro;
import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeLibros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LibrosServicio {

    private final RepositorioDeLibros repositorioDeLibros;
    @Autowired
    public LibrosServicio(RepositorioDeLibros repositorioDeLibros, AutoresServicio autoresServicio) {
        this.repositorioDeLibros = repositorioDeLibros;
    }

    public List<Libro> librosPorLenguaje (String lenguaje) {
        List<Libro> libros = repositorioDeLibros.getLibrosByLenguaje(lenguaje);
        if (libros.isEmpty()) {
            // Manejo de caso donde no se encuentran libros
            System.out.println("No se encontraron libros en el idioma: " + lenguaje);
        }
        return libros;
    }

}
