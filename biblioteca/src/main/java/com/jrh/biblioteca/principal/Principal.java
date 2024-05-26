package com.jrh.biblioteca.principal;

import com.jrh.biblioteca.model.APIResponse;
import com.jrh.biblioteca.model.Autor;
import com.jrh.biblioteca.model.DatosLibro;
import com.jrh.biblioteca.model.Libro;
import com.jrh.biblioteca.service.ConsumoAPI;
import com.jrh.biblioteca.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private static Scanner entrada = new Scanner(System.in);
    private Libro libro;
    private List <Libro> libros;
    private List <Autor> autores;
    private Optional<Autor> autor;


    public  void menu() {
        var opcionMenu = -1;

        while(opcionMenu != 0){
            var menuVisual = """
                    1 - Consultar Libro
                    0 - Salir
                    """;
            try{
                System.out.println(menuVisual);
                opcionMenu = entrada.nextInt();
                entrada.nextLine();
            }catch(InputMismatchException e){
                entrada.nextLine();
                opcionMenu=-1;
            }
            switch(opcionMenu){
                case 1:
                    ConsultarLibro();;
                    break;
                default:
                    System.out.println("gracias");
            }

        }
    }

    private void ConsultarLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = entrada.nextLine();
        var json = consumoAPI.getData("https://gutendex.com/books/?search="+nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.getData(json, APIResponse.class);
        Optional<DatosLibro> libroEncontrado = datosBusqueda.Libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();
        System.out.println(libroEncontrado.get());

    }
}
