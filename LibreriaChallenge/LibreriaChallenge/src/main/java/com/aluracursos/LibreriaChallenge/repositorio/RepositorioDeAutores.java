package com.aluracursos.LibreriaChallenge.repositorio;

import com.aluracursos.LibreriaChallenge.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDeAutores extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anioBuscado AND (a.fechaDeMuerte >= :anioBuscado OR a.fechaDeMuerte IS NULL)")
    List<Autor> autoresPorAnio(@Param("anioBuscado") Integer anioBuscado);

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libro")
    List<Autor> encontrarTodosLosAutores();

    @Query("SELECT a FROM Autor a WHERE " +
            "(" +
            "  (a.fechaDeNacimiento IS NOT NULL AND a.fechaDeNacimiento <= :year) OR " +
            "  (a.fechaDeNacimiento = 0)" +
            ") AND (" +
            "  a.fechaDeMuerte IS NULL OR " +
            "  a.fechaDeMuerte >= :year OR " +
            "  a.fechaDeMuerte = 0" +
            ")")
    Optional<Autor> findByNombreAutor(String nombreAutor);

}


