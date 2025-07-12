package com.alura.appliteratura.principal;

import com.alura.appliteratura.model.*;
import com.alura.appliteratura.repository.AutorRepository;
import com.alura.appliteratura.repository.LibroRepository;
import com.alura.appliteratura.service.ConsumoApi;
import com.alura.appliteratura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();
    private String URL_BASE = "https://gutendex.com";
    private ConvierteDatos convierteDatos = new ConvierteDatos();

    private List<DatosLibros> librosBuscados = new ArrayList<>();

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;



    public void mostrarMenu(){
        var opcion = -1;
        while(opcion != 0){
            var menu = """
                    1 - Buscar libros por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - salir
                    """;

            System.out.println(menu);
            var input = teclado.nextLine();
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
                opcion = -1; // fuerza que se repita el menú
            }

            switch (opcion){
                case 1 :
                    buscarLibroWeb();
                    break;
                case 2 :
                    listarLibrosRegistrados();
                    break;
                case 3 :
                    listarAutoresRegistrados();
                    break;
                case 4 :
                    buscarAutoresVivosEnAnio();
                    break;
                case 5 :
                    listarLibrosPorIdioma();
                    break;
                case 0 :
                    System.out.println("Saliendo de la aplicacion");
                    break;
                default:
                    System.out.println("Opcion invalida");

            }
        }
    }

    private DatosLibros buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
         var nombreLibro = teclado.nextLine();
         var json = consumoApi.obtenerDatos(URL_BASE+"/books?search="+nombreLibro.replace(" ","+"));
         Result resultado = convierteDatos.obtenerDatos(json,Result.class);
            var libro = resultado.results().stream()
                    .filter(l -> l.titulo().equalsIgnoreCase(nombreLibro))
                    .findFirst()
                    .orElse(null);

            if (libro == null) {
                System.out.println("Libro no encontrado");
                return null;
            } else {
                librosBuscados.add(libro);
                System.out.println("--------------------------------------");
                System.out.println("\n📗 Libro encontrado:\n");
                System.out.println("--------------------------------------");
                System.out.println("🔸 Título: " + libro.titulo());
                System.out.println("✍ Autor: " + libro.getPrimerAutor().nombre());
                System.out.println("🌐 Idiomas: " + String.join(", ", libro.idiomas()));
                System.out.println("⬇ Descargas: " + libro.numeroDescargas());
                System.out.println("-------------------------------------");

                return libro;
            }

    }

    private void buscarLibroWeb() {
        DatosLibros datos = buscarLibroPorTitulo();
        if (datos == null) return;

        DatosAutor datosAutor = datos.getPrimerAutor();
        Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre())
                .orElse(null);

        if (autor == null) {
            autor = new Autor(datosAutor);
            autor = autorRepository.save(autor); // ¡Usamos el resultado del save!
        }

        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(datos.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("Ese libro ya fue guardado.");
            return;
        }


        Libro libro = new Libro(datos, autor);
        libroRepository.save(libro);

        System.out.println("Libro guardado: " + datos.titulo());
    }


//    private List<DatosLibros> buscarTodosLosLibros() {
//        var json = consumoApi.obtenerDatos(URL_BASE.replace(" ", "+")+"/books");
//        Result datos = convierteDatos.obtenerDatos(json, Result.class);
//        System.out.println(json);
//        return datos.results();
//    }

    private void buscarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        int anio = Integer.parseInt(teclado.nextLine());

        String url = URL_BASE + "/books?author_year_start=" + anio + "&author_year_end=" + anio;
        var json = consumoApi.obtenerDatos(url);
        var resultado = convierteDatos.obtenerDatos(json, Result.class);

        resultado.results().stream()
                .map(DatosLibros::getPrimerAutor)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(System.out::println);

    }

    public void listarAutoresBuscados() {
        if (librosBuscados.isEmpty()) {
            System.out.println("No hay libros buscados aún.");
            return;
        }
        librosBuscados.stream()
                .map(DatosLibros::getPrimerAutor)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(System.out::println);
    }

    public void listarLibrosRegistrados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        for (Libro libro : libros) {
            System.out.println(" Título: " + libro.getTitulo());
            System.out.println(" Autor: " + libro.getAutor().getNombre());
            System.out.println(" Idioma: " + libro.getIdioma());
            System.out.println(" Descargas: " + libro.getDescargas());
            System.out.println("-------------------------------------");
        }
    }

    public void listarAutoresRegistrados(){
        List<Autor> autores = autorRepository.findAll();
        if(autores.isEmpty()){
            System.out.println("No hay autores registrados");
            return;
        }

        for (Autor autor : autores){
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getFechaNac());
            System.out.println("Año de fallecimiento: " + autor.getAnioFallecimiento());
            System.out.println("---------------------------------------------------------");
        }
    }

    public void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma a buscar (ej: en, es, fr):");
        String idioma = teclado.nextLine();

        List<Libro> libros = libroRepository.findByIdiomaIgnoreCase(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma);
            return;
        }

        System.out.println("\n📘 Libros en idioma '" + idioma + "':\n");

        for (Libro libro : libros) {
            System.out.println(" Título: " + libro.getTitulo());
            System.out.println(" Autor: " + libro.getAutor().getNombre());
            System.out.println(" Descargas: " + libro.getDescargas());
            System.out.println("-----------------------------------");
        }
    }



}
