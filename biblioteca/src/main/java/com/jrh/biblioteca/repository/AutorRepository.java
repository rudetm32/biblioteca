package com.jrh.biblioteca.repository;

import com.jrh.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AutorRepository extends JpaRepository<Autor, Long> {

    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a " +
            "WHERE a.fechaNacimiento < :finPeriodo " +
            "AND (a.fechaDefuncion IS NULL OR a.fechaDefuncion > :inicioPeriodo)")
    List<Autor> buscarAutoresVivos(@Param("inicioPeriodo") Integer inicioPeriodo, @Param("finPeriodo") Integer finPeriodo);

}
