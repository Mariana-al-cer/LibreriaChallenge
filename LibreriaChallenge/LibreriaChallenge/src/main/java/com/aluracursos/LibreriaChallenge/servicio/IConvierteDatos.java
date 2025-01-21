package com.aluracursos.LibreriaChallenge.servicio;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
