package com.aluracursos.LibreriaChallenge.modelo;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(unique = true)
    private String titulo;

    @Column(name = "lenguaje")
    private String lenguaje;

    @ElementCollection(fetch = FetchType.EAGER) // Cambiar a EAGER
    @CollectionTable(name = "libro_descripcion", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "descripcion")
    private List<String> descripcionLibro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {
        // Constructor vacío para JPA
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.lenguaje = datosLibro.lenguajes().isEmpty() ? "No disponible" : datosLibro.lenguajes().get(0);
        this.descripcionLibro = datosLibro.descripcionLibro();
        this.autor = autor; // Asigna el autor directamente
    }

    @Override
    public String toString() {
        return "Titulo='" + titulo + '\'' +
                ", Autor=" + autor +
                ", Idioma=" + lenguaje +
                ", Temas='" + descripcionLibro;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public List<String> getDescripcionLibro() {
        return descripcionLibro;
    }

    public void setDescripcionLibro(List<String> descripcionLibro) {
        this.descripcionLibro = descripcionLibro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}

