package com.jrh.biblioteca.service;

import java.util.Map;

public interface IConvierteDatos {
        <T> T getData(String json, Class<T> clase);
}
