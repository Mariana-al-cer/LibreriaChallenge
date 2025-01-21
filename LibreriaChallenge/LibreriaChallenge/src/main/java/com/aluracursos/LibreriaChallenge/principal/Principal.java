package com.aluracursos.LibreriaChallenge.principal;

import com.aluracursos.LibreriaChallenge.modelo.*;
import com.aluracursos.LibreriaChallenge.servicio.ConsumoAPI;
import com.aluracursos.LibreriaChallenge.servicio.ConvierteDatos;
import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeAutores;
import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeLibros;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Component
public class Principal {
    @Autowired
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    @Autowired
    private RepositorioDeLibros repositorioDeLibros;
    @Autowired
    private RepositorioDeAutores repositorioDeAutores;

    public Principal(Scanner teclado, RepositorioDeLibros repositorioDeLibros, RepositorioDeAutores repositorioDeAutores ) {
        this.teclado = teclado;
        this.repositorioDeLibros = repositorioDeLibros;
        this.repositorioDeAutores= repositorioDeAutores;
    }

    public void muestraElMenu() {
        boolean bandera;
        int opcion = 0;

        do {
            System.out.println("----------------------------------");
            System.out.println("Bienvenido a nuestra libreria");
            System.out.println("----------------------------------");
            System.out.println("----------------------------------");
            System.out.println("Por favor seleccione el numero de la opcion que desea consultar");
            System.out.println("----------------------------------");
            System.out.println("1. Escriba el titulo del libro que desea buscar");
            System.out.println("2. Lista de libros disponibles");
            System.out.println("3. Lista de autores disponibles ");
            System.out.println("4. Autores que publicaron en un año determinado");
            System.out.println("5. Libros disponibles por idioma");
            System.out.println("6. Salida");
            System.out.println("----------------------------------");
            bandera = false;
            while (!bandera) {
                System.out.print("Por favor ingrese el numero de la opcion que desea consultar: ");
                try {
                    opcion = teclado.nextInt();
                    teclado.nextLine();
                    if (opcion > 0 && opcion < 7) {
                        bandera = true;
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Dato incorrecto, porfavor ingrese un numero de la lista");
                    teclado.next();
                    teclado.nextLine();
                }
            }


            switch (opcion) {
                case 1:
                    tituloLibro();
                    break;
                case 2:
                    mostrarLibrosDisponibles();
                    break;
                case 3:
                    mostrarAutoresDisponibles();
                    break;
                case 4:
                    System.out.println("Ingrese el año que desea consultar: ");
                    buscarAutorPorAnio();
                    break;
                case 5:
                    System.out.println("Ingrese el idioma en el que desea consultar los libros disponibles : ");
                    buscarLibrosPorLenguaje();
                    break;
                case 6:
                    System.out.println("Salida");
                    bandera = true; // Esto hace que el bucle se detenga
                    System.exit(0); // Cierra el programa
                    break;
            }
        } while (opcion != 6);

    }

    private void tituloLibro() {
        System.out.println("Ingrese el título del libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();
        if (tituloLibro == null || tituloLibro.trim().isEmpty()) {
            System.out.println("Por favor, ingrese un título válido.");
            return;
        }

        try {
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + URLEncoder.encode(tituloLibro, StandardCharsets.UTF_8));
            RespuestaDatosLibro respuesta = conversor.obtenerDatos(json, RespuestaDatosLibro.class);

            if (respuesta == null || respuesta.results() == null || respuesta.results().isEmpty()) {
                System.out.println("El libro no se encuentra disponible, por favor elija otro.");
                return;
            }

            System.out.println("El libro se encuentra disponible:");
            System.out.println("--------------------------------------------------");

            for (var libro : respuesta.results()) {
                // Crear una lista de DatosAutor
                List<DatosAutor> autores = libro.autor().stream()
                        .map(a -> new DatosAutor(
                                null, // ID generado automáticamente
                                a.nombreAutor(),
                                a.fechaDeNacimiento(),
                                a.fechaDeMuerte()
                        ))
                        .collect(Collectors.toList());

                // Verificar y guardar el autor
                Autor autor = null;
                for (DatosAutor datosAutor : autores) {
                    Optional<Autor> autorExistente = repositorioDeAutores.findByNombreAutor(datosAutor.nombreAutor());
                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get(); // Usar el autor existente
                    } else {
                        autor = new Autor(datosAutor); // Crear un nuevo autor
                        repositorioDeAutores.save(autor); // Guardar el nuevo autor
                    }
                }


                // Crear un objeto DatosLibro
                DatosLibro datosLibro = new DatosLibro(
                        null, // No necesitas pasar el ID aquí si se genera automáticamente
                        libro.titulo(),
                        autores,
                        libro.lenguajes(),
                        libro.descripcionLibro()
                );

                // Verificar si el libro ya existe

                // Verificar si el libro ya existe
                Optional<Libro> libroExistente = repositorioDeLibros.findByTitulo(datosLibro.titulo());
                if (libroExistente.isPresent()) {
                    // Actualizar el libro existente
                    Libro libroToUpdate = libroExistente.get();
                    libroToUpdate.setLenguaje(datosLibro.lenguajes().isEmpty() ? "No disponible" : datosLibro.lenguajes().get(0));
                    libroToUpdate.setDescripcionLibro(datosLibro.descripcionLibro());
                    libroToUpdate.setAutor(autor); // Asegúrate de que 'autor' esté correctamente asignado

                    try {
                        repositorioDeLibros.save(libroToUpdate); // Guardar el libro actualizado
                        System.out.println("Libro actualizado: " + libroToUpdate.getTitulo());
                    } catch (Exception e) {
                        System.err.println("Error al actualizar el libro: " + e.getMessage());
                    }
                } else {
                    // Crear el nuevo libro
                    Libro nuevoLibro = new Libro(datosLibro);
                    try {
                        repositorioDeLibros.save(nuevoLibro); // Guardar el nuevo libro
                        System.out.println("Título: " + nuevoLibro.getTitulo());
                        System.out.println("Autor: " + nuevoLibro.getAutor());
                        System.out.println("Descripción: " + String.join(", ", nuevoLibro.getDescripcionLibro()));
                        System.out.println("Lenguajes: " + String.join(", ", nuevoLibro.getLenguaje()));
                    } catch (DataIntegrityViolationException e) {
                        System.err.println("Error: El libro ya existe en la base de datos: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error al guardar el libro: " + e.getMessage());
                    }
                }

                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener datos de la API: " + e.getMessage());
        }
    }

    @Transactional
    private void mostrarLibrosDisponibles() {
        try {
            List<Libro> todosLosLibros = repositorioDeLibros.findAll();

            System.out.println("\n\n LIBROS DE LA BASE DE DATOS: \n");
            if (todosLosLibros.isEmpty()) {
                System.out.println("No hay libros disponibles en la base de datos.");
            } else {
                todosLosLibros.forEach(l -> {
                    // Inicializa la colección antes de acceder a ella
                    Hibernate.initialize(l.getDescripcionLibro());
                    System.out.println(
                            """ 
                                    TÍTULO : %s - %s - Idiomas: %s - Descripciones: %s   
                                    """.formatted(
                                    l.getTitulo() != null ? l.getTitulo() : "Título no disponible",
                                    l.getAutor() != null ? l.getAutor().getNombreAutor() : "Autor no disponible",
                                    l.getLenguaje() != null ? l.getLenguaje() : "Idioma no disponible",
                                    l.getDescripcionLibro() != null ? String.join(", ", l.getDescripcionLibro()) : "Descripción no disponible"
                            ));
                });
            }
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace(); // Esto puede ayudar en la depuración
        }
    }

    private void mostrarAutoresDisponibles () {
        List<Autor> todosLosAutores = repositorioDeAutores.encontrarTodosLosAutores();

        System.out.println("\n\n AUTORES DE LA BASE DE DATOS: \n");
        todosLosAutores.forEach(a -> System.out.println(
                """ 
                        NOMBRE : %s - Fecha de Nacimiento: %s - Fecha de Muerte: %s
                        """.formatted(a.getNombreAutor(), a.getFechaDeNacimiento(), a.getFechaDeMuerte()
                )));
    }

    private void buscarAutorPorAnio () {
        System.out.println("Escribe el año en que deseas buscar los autores: ");
        var anioDeBusqueda = teclado.nextLine();

        // Validar que el año sea un número
        try {
            int anio = Integer.parseInt(anioDeBusqueda);
            List<Autor> autorEncontrados = repositorioDeAutores.autoresPorAnio(anio);

            if (autorEncontrados.isEmpty()) {
                System.out.println("No se encontraron autores para el año " + anio + ".");
            } else {
                autorEncontrados.forEach(a -> System.out.println(
                        "NOMBRE: " + a.getNombreAutor() + " - Fecha de Nacimiento: " + a.getFechaDeNacimiento() + " - Fecha de Muerte: " + a.getFechaDeMuerte()));
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingresa un año válido.");
        }
    }

    private void buscarLibrosPorLenguaje () {
        System.out.println("Ingrese el idioma en el que desea consultar los libros disponibles: ");
        var lenguaje = teclado.nextLine();

        List<Libro> libroPorLenguaje = repositorioDeLibros.getLibrosByLenguaje(lenguaje);

        if (libroPorLenguaje.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + lenguaje + ".");
        } else {
            libroPorLenguaje.forEach(System.out::println);
        }
    }

}