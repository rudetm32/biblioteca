package com.jrh.biblioteca.service;

import com.jrh.biblioteca.model.Autor;
import com.jrh.biblioteca.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public Autor guardarAutor (Autor autor) {

        return autorRepository.save(autor);
    }
}
