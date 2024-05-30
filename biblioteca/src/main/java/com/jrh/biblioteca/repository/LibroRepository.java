package com.jrh.biblioteca.repository;


import com.jrh.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByTitulo(String titulo);

    List<Libro> findByidiomaContaining(String idioma);

}