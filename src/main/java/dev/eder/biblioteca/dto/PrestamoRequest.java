package dev.eder.biblioteca.dto;

import jakarta.validation.constraints.*;

public record PrestamoRequest(
        // el id del PADRE: qué libro se presta
        @NotNull(message = "El libro es obligatorio")
        @Positive(message = "El id del libro debe ser positivo")
        Long libroId,

        @NotBlank(message = "El DNI del lector es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
        String lectorDni,

        @NotBlank(message = "El nombre del lector es obligatorio")
        @Size(max = 120, message = "El nombre tiene máximo 120 caracteres")
        String lectorNombre,

        @NotNull(message = "Los días de préstamo son obligatorios")
        @Min(value = 1, message = "Mínimo 1 día")
        @Max(value = 30, message = "Máximo 30 días")
        Integer dias
) {
}
