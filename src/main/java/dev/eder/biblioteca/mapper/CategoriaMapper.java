package dev.eder.biblioteca.mapper;

import dev.eder.biblioteca.dto.CategoriaRequest;
import dev.eder.biblioteca.dto.CategoriaResponse;
import dev.eder.biblioteca.entidades.Categoria;

public class CategoriaMapper {
    private CategoriaMapper(){}

    public static Categoria toEntity(CategoriaRequest request){
        Categoria categoria = new Categoria();
        actualizarEntidad(request, categoria);
        return categoria;
    }

    public static void actualizarEntidad(CategoriaRequest request, Categoria categoria){
        categoria.setNombre(normalizarNombre(request.nombre()));
        // la descripción es opcional: si no llega, se guarda null
        categoria.setDescripcion(request.descripcion() == null ? null : request.descripcion().trim());
    }

    public static CategoriaResponse toResponse(Categoria categoria){
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .fechaRegistro(categoria.getFechaRegistro())
                .build();
    }
    // "  novela   negra " -> "novela negra"
    public static String normalizarNombre(String nombre){
        return nombre.trim().replaceAll("\\s+", " ");
    }
}
