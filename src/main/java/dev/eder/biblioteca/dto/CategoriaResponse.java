package dev.eder.biblioteca.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder //Genera el patrón Builder: Clase.builder().campo(valor).build().
public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        LocalDateTime fechaRegistro
) {
}
