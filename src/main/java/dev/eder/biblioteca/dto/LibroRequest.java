package dev.eder.biblioteca.dto;

import jakarta.validation.constraints.*;

public record LibroRequest(

        @NotBlank(message = "EL isbn es obligatorio")
        @Pattern(regexp = "^\\d{13}", message = "El ISBN debe tener 13 dígitos")
        String isbn,

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El título tiene máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 100, message = "El autor tiene máximo 100 caracteres")
        String autor,

        @NotNull(message = "El año de publicacion es obligatorio")
        @Min(value = 1450, message = "El año no puede ser menor a 1450 ")
        @Max(value = 2100, message = "El áño no es valido")
        Integer anoPublicado,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no debe de ser negativo")
        @Max(value = 100, message = "El stock máximo es 100")
        Integer stock,

        // el id del PADRE: a qué categoría pertenece el libro
        @NotNull(message = "La cateogira es obligatoria")
        @Positive(message = "El id de la categoria debe ser positivo")
        Long categoriaId
) {
}
