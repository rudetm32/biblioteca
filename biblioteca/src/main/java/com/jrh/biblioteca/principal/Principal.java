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
                    consultarLibro();
                    break;
                case 2:
                    consultarTodosLosLibros();
                    break;
                case 3:
                    consultarAutoresRegistrados();
                    break;
                case 4:
                    consultarAutoresVivosPorAnio();
                case 5:
                    consultarLibrosPorIdioma();
                case 0:
                    System.out.println("Gracias por su visita. ¡ Hasta pronto !");
                    break;
                default:
                    System.out.println("Eliga una opcion valida");
            }

        }
    }

    private void consultarLibro(){
        System.out.println("Ingrese el titulo del libro: ");
        var nombreLibro = entrada.nextLine();
        var json = consumoAPI.getData(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.getData(json, APIResponse.class);

        Optional<DatosLibro> libroEncontrado = datosBusqueda.Libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if(libroEncontrado.isPresent()) {
            DatosLibro datosLibro = libroEncontrado.get();
            Optional<Libro> libroExistente = libroService.buscarPorTitulo((datosLibro.titulo()));

            if(libroExistente.isEmpty()) {
                Libro libro = new Libro(datosLibro);
                libroService.guardarLibro(libro);
                mostrarDatosLibro(datosLibro);

            } else {
                System.out.println("El libro ya existe en la db");

            }
        } else {
            System.out.println("Titulo del libro inexistente en la db");

        }
    }
    private void consultarTodosLosLibros() {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        if(libros.isEmpty()){
            System.out.println("No hay libros registrados");
        } else {
            libros.sort(Comparator.comparing(Libro::getTitulo));

            System.out.println("\nLibros registrados : \n");
            libros.forEach(libro -> {
                String autores = libro.getAutores().stream()
                        .map(Autor::getNombre)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Desconocido");

                String output = """
                        -------------------------------------------------
                        Título       : %s
                        Autores      : %s
                        Lenguaje     : %s
                        -------------------------------------------------
                        """.formatted(libro.getTitulo(), autores, libro.getIdioma().get(0));

                System.out.println(output);
           });
            System.out.println("Total de registros en la db: " + libros.size());
        }
    }

    private void consultarAutoresRegistrados() {
        System.out.println("autores por año");
    }

    private void consultarAutoresVivosPorAnio() {
        System.out.println("autores vivos");
    }

    private void consultarLibrosPorIdioma() {
        System.out.println("libros por idioma");
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
        System.out.println("-------------------------------------------------\n");
    }

}
