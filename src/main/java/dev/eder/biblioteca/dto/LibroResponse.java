package dev.eder.biblioteca.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LibroResponse(
        Long id,
        String isbn,
        String titulo,
        String autor,
        Integer anioPublicacion,
        Integer stock,
        boolean disponible,
        Long categoriaId,
        String categoriaNombre,
        String descripcion,
        LocalDateTime fechaRegistro
) {

}
