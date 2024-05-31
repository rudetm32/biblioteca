package com.jrh.biblioteca.principal;


import com.jrh.biblioteca.model.*;
import com.jrh.biblioteca.repository.*;
import com.jrh.biblioteca.service.*;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private List<Libro> libros;
    private LibroRepository repository;
    private AutorRepository autorRepository;

    private String head = """
            ---------------------------------------------------
                           *****  Resultados  *****      
            --------------------------------------------------- 
                """;
    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.repository = libroRepository;
    }

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            var menu = """
                
                ----------------------------------------------------
                            *****  Menu Principal  *****
                ---------------------------------------------------- 
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado periodo
                5 - Listar libros por idiomas
                
                0 - Salir
                ---------------------------------------------------- 
                """;

            System.out.println(menu);
            try {
                System.out.println("Ingrese  una opcion: ");
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarTodosLosLibros();
                        break;
                    case 3:
                        listarTodosLosAutores();
                        break;
                    case 4:
                        autoresVivosPorPeriodo();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingresa una opcion valida.");
                teclado.nextLine();
            }
        }
    }

    private RespuestaAPI getDatosLibros() {
        System.out.println("Ingrese el titulo del libro: ");
        var libro = teclado.nextLine();
        var json = consumoAPI.consumirDatos(URL_BASE + "?search=" + libro.replace(" ", "+"));
        System.out.println(json);
        RespuestaAPI datos = convierteDatos.obtenerDatos(json, RespuestaAPI.class);
        return datos;
    }

    private Libro crearLibro(DatosLibros datosLibros, Autor autor) {
        Libro libro = new Libro(datosLibros, autor);
        return repository.save(libro);
    }
    private void buscarLibroPorTitulo() {
        RespuestaAPI datos = getDatosLibros();

        if (!datos.libros().isEmpty()) {
            DatosLibros datosLibros = datos.libros().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);
            Libro libroDb = repository.findByTitulo(datosLibros.titulo());

            if (libroDb != null) {
                System.out.println(head);
                mostrarDetallesLibro(libroDb);
            } else {
                Autor autorDb = autorRepository.findByNombre(datosAutor.nombre());
                Libro libro;
                if (autorDb == null) {
                    Autor autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                    libro = crearLibro(datosLibros, autor);

                } else {
                    libro = crearLibro(datosLibros, autorDb);

                }
                System.out.println(head);
                mostrarDetallesLibro(libro);
            }
        } else {
            System.out.println("No se encontraron libros.");
        }
    }

    private void listarTodosLosLibros() {
        libros = repository.findAll();

        System.out.println(head);
        libros.forEach(this::mostrarDetallesLibro); // Llama a mostrarDetallesLibro para cada libro

    }

    private void listarTodosLosAutores() {
        List<Autor> autores = autorRepository.findAll();

        System.out.println(head);
        autores.forEach(autor -> {
            System.out.println("---------------------------------------------------");
            System.out.println("Nombre: " + autor.getNombre());

            String titulosLibros = autor.getLibro().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.joining(" || "));

            System.out.println("Libros: " + titulosLibros);
            System.out.println("---------------------------------------------------");
        });
    }


    private void autoresVivosPorPeriodo() {
        System.out.println("Ingrese fecha inicial (YYYY):");
        Integer inicioPeriodo = teclado.nextInt();
        System.out.println("Ingrese fecha final (YYYY):");
        Integer finPeriodo = teclado.nextInt();

        try {

            List<Autor> autoresVivos = autorRepository.buscarAutoresVivos(inicioPeriodo, finPeriodo);
            if(autoresVivos.isEmpty()){
                System.out.println("\nNo hay autores vivos en ese periodo");
            } else {
                System.out.println(head);
                for (Autor autor : autoresVivos) {
                    System.out.println("Nombre: " + autor.getNombre());
                    System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
                    System.out.println("Fecha de fallecimiento: " + (autor.getFechaDefuncion() != null ? autor.getFechaDefuncion() : "N/A"));
                    System.out.println("-------------------------");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void listarLibrosPorIdioma() {
        Map<Integer, String> opcionesIdioma = Map.of(
                1, "en",
                2, "es",
                3, "fr",
                4, "al"
        );

        while (true) {
            System.out.println("""
            ---------------------------------------------------
                   *****  Elige el idioma a buscar  *****      
            --------------------------------------------------- 
            1 - Inglés
            2 - Español
            3 - Francés
            4 - Alemán
            
            5 - Volver al menú principal
            ---------------------------------------------------
            """
            );

            int opcion = -1;
            try {
                System.out.println("Ingrese  una opcion: ");
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingresa dato valido.");
                teclado.nextLine();
                continue;
            }

            if (opcion == 5) {
                System.out.println("Regresando al menú principal...");
                break;
            }

            String idioma = opcionesIdioma.get(opcion);
            if (idioma != null) {
                List<Libro> libros = repository.findByidiomaContaining(idioma);
                if (libros.isEmpty()) {
                    System.out.println("No se encontraron libros en el idioma seleccionado.");
                } else {
                    System.out.println(head);
                    libros.forEach(libro -> System.out.printf(
                            " Codigo idioma: %s   Titulo: %s%n",  libro.getIdioma(), libro.getTitulo()));
                    System.out.println("----------------------------------------------------\n");

                }
            } else {
                System.out.println("Opción no válida, por favor intente de nuevo.");
            }
        }
    }

    private void mostrarDetallesLibro(Libro libro) {
        String detalleLibro = """
            Título: %s
            Autor: %s
            Idioma: %s
            Descargas: %d
            ---------------------------------------------------
            """;
        System.out.printf(detalleLibro,
                libro.getTitulo(),
                libro.getAutor().getNombre(),
                libro.getIdioma(),
                libro.getNumeroDeDescargas());
    }
}