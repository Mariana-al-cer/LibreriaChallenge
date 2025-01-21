package com.aluracursos.LibreriaChallenge.modelo;

import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeLibros;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class GuardarLibro {

    private RepositorioDeLibros repositorioDeLibros;

    private DatosLibro datosLibro;

    @Transactional
    public void guardarLibro(Libro libro) {
        // Verificar si el libro ya existe
        Optional<Libro> libroExistente = repositorioDeLibros.findByTitulo(datosLibro.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("El libro '" + datosLibro.titulo() + "' ya existe en la base de datos.");
            return; // O maneja esto de la manera que desees
        }

        // Crear el nuevo libro
        Libro nuevoLibro = new Libro(datosLibro);
        repositorioDeLibros.save(nuevoLibro);
    }
}