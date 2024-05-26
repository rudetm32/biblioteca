package com.jrh.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Autor {
    private String nombre;
    private Integer anioDeNacimiento;
    private Integer AnioDeFallecimiento;


    public Autor(){}
    public Autor(String nombre, Integer anioDeNacimiento, Integer anioDeFallecimiento) {
        this.nombre = nombre;
        this.anioDeNacimiento = anioDeNacimiento;
        this.AnioDeFallecimiento = anioDeFallecimiento;
    }

    public Autor(String nombreAutor) {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioDeNacimiento() {
        return anioDeNacimiento;
    }

    public void setAnioDeNacimiento(Integer anioDeNacimiento) {
        this.anioDeNacimiento = anioDeNacimiento;
    }

    public Integer getAnioDeFallecimiento() {
        return AnioDeFallecimiento;
    }

    public void setAnioDeFallecimiento(Integer anioDeFallecimiento) {
        AnioDeFallecimiento = anioDeFallecimiento;
    }
}
