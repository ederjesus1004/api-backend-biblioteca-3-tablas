package dev.eder.biblioteca.mapper;

import dev.eder.biblioteca.dto.LibroRequest;
import dev.eder.biblioteca.dto.LibroResponse;
import dev.eder.biblioteca.entidades.Categoria;
import dev.eder.biblioteca.entidades.Libro;

public class LibroMapper {
    private LibroMapper(){};

    // recibe la categoría YA BUSCADA por el service: el mapper no consulta la BD
    public static Libro toEntity(LibroRequest request, Categoria categoria){
        Libro libro = new Libro();
        actualizarEntidad(request, libro, categoria);
        return libro;
    }

    public static void actualizarEntidad(LibroRequest request, Libro libro, Categoria categoria){
        libro.setIsbn(request.isbn().trim());
        libro.setTitulo(request.titulo().trim());
        libro.setAutor(request.autor().trim());
        libro.setAnoPublicado(request.anoPublicado());
        libro.setStock(request.stock());
        libro.setCategoria(categoria);
    }

    public static LibroResponse toResponse(Libro libro){
        return LibroResponse.builder()
                .id(libro.getId())
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .anioPublicacion(libro.getAnoPublicado())
                .stock(libro.getStock())
                .disponible(libro.getStock() > 0)
                .categoriaId(libro.getCategoria().getId())
                .categoriaNombre(libro.getCategoria().getNombre())
                .descripcion(armarDescripcion(libro))
                .fechaRegistro(libro.getFechaRegistro())
                .build();

    }
    // "Cien años de soledad - Gabriel García Márquez (1967)"
    public static String armarDescripcion(Libro libro){
        return libro.getTitulo() + " - " + libro.getAutor() + " (" + libro.getAnoPublicado() + ") ";
    }
}
