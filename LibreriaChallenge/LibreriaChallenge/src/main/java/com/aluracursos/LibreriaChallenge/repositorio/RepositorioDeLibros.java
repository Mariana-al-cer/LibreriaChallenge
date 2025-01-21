package com.aluracursos.LibreriaChallenge.repositorio;

import com.aluracursos.LibreriaChallenge.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface RepositorioDeLibros extends JpaRepository <Libro, Long> {

List<Libro> findAll();


@Query("SELECT l FROM Libro l WHERE l.lenguaje = :lenguaje")
List<Libro> getLibrosByLenguaje(@Param("lenguaje") String lenguaje);

/* el nombre del atributo id en la clase Autor debe estar en minúscula,
ya que en JPQL se hace referencia a los atributos de la entidad y no a los
nombres de las columnas de la base de datos*/

    @Query("SELECT l FROM Libro l WHERE l.autor.id = :idAutor")
    List<Libro> getLibrosByAutor(@Param("idAutor") Long idAutor);

    Optional<Libro> findByTitulo(String titulo);

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllWithAutores();

}

