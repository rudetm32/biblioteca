package com.jrh.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {
   private String titulo;
   private List<Autor> autor;
   private Integer descargas;
   private String idioma;


    public Libro(){}
    public Libro(String titulo, List<Autor> autor, Integer descargas, String idioma) {
        this.titulo = titulo;
        this.autor = autor;
        this.descargas = descargas;
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

}
