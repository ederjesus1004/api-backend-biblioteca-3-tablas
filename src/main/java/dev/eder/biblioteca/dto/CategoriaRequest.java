package dev.eder.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "el nombre es obligatorio")
        @Size(max = 60, message = "El nombre tiene máximo 60 caracteres")
        String nombre,

        // opcional: puede venir vacío o no venir
        @Size(max = 200, message = "La descripción tiene máximo 200 caracteres")
        String descripcion
) {
}
