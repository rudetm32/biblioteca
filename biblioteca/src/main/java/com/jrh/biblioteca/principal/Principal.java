package com.jrh.biblioteca.principal;

import com.jrh.biblioteca.model.APIResponse;
import com.jrh.biblioteca.model.Autor;
import com.jrh.biblioteca.model.DatosLibro;
import com.jrh.biblioteca.model.Libro;
import com.jrh.biblioteca.service.AutorService;
import com.jrh.biblioteca.service.ConsumoAPI;
import com.jrh.biblioteca.service.ConvierteDatos;
import com.jrh.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Principal {

    @Autowired
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    @Autowired
    private ConvierteDatos conversor = new ConvierteDatos();
    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;

    private static Scanner entrada = new Scanner(System.in);
    
    private final String URL_BASE ="https://gutendex.com/books/";
    private Libro libro;
    private List <Libro> libros;
    private List <Autor> autores;
    private Optional<Autor> autor;


    public  void menu() {
        var opcionMenu = -1;

        while(opcionMenu != 0){
            var menuVisual = """
                    -------------------------------------------------
                      ***   Sistema de consulta de literatura   ***
                    -------------------------------------------------
                    
                    1 - Consultar Libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    -------------------------------------------------
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
                case 0:
                    System.out.println("Gracias por su visita. ¡ Hasta pronto !");
                    break;
                default:
                    System.out.println("Eliga una opcion valida");
            }

        }
    }

    private void ConsultarLibro(){
        System.out.println("Ingrese el titulo del libro: ");
        var nombreLibro = entrada.nextLine();
        var json = consumoAPI.getData(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.getData(json, APIResponse.class);

        Optional<DatosLibro> libroEncontrado = datosBusqueda.Libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if(libroEncontrado.isPresent()) {
            DatosLibro datosLibro = libroEncontrado.get();
            Libro libro = new Libro(datosLibro);
            libroService.guardarLibro(libro);
            mostrarDatosLibro(datosLibro);
        }else {
            System.out.println("Titulo del libro inexistente en la db");
        }

    }

    private void mostrarDatosLibro(DatosLibro libro) {
        System.out.println("-------------------------------------------------");
        System.out.println("Título       : " + libro.titulo());

        String autores = libro.autor().get(0).nombre();
        System.out.println("Autores      : " + autores);

        String primerIdioma = libro.idioma().stream()
                .findFirst()
                .orElse("Desconocido");
        System.out.println("Lenguaje     : " + primerIdioma);
        System.out.println("Descargas    : " + libro.descargas());

        System.out.println();
        System.out.println("-------------------------------------------------");
    }

}
