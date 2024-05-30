package com.jrh.biblioteca.service;

public interface IConvierteDatos {
        <T> T obtenerDatos(String json, Class<T> classe);
}