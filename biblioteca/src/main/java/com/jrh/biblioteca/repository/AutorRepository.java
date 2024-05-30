package com.jrh.biblioteca.repository;

import com.jrh.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AutorRepository extends JpaRepository<Autor, Long> {

    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE SUBSTRING(a.fechaNacimiento, 1, 4) < :finPeriodo AND (a.fechaDefuncion IS NULL OR SUBSTRING(a.fechaDefuncion, 1, 4) > :inicioPeriodo)")
    List<Autor> autorVivoEnPeriodo(@Param("inicioPeriodo") String inicioPeriodo, @Param("finPeriodo") String finPeriodo);

}