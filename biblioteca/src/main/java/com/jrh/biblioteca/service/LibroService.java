package com.jrh.biblioteca.service;

import com.jrh.biblioteca.model.Libro;
import com.jrh.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = libroRepository.buscaConIdioma();
        libros.forEach(libro -> {
            libro.getAutores().size();
        });
        return libros;
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    public Optional<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTitulo(titulo);
    }
}
