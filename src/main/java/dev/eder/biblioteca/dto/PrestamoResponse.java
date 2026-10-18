package dev.eder.biblioteca.dto;

import dev.eder.biblioteca.entidades.EstadoPrestamo;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PrestamoResponse(
        Long id,
        Long libroId,
        String libroTitulo,
        String lectorDni,
        String lectorNombre,
        LocalDate fechaPrestamo,
        LocalDate fechaLimite,
        LocalDate fechaDevolucion,
        EstadoPrestamo estado,
        boolean vencido

) {
}
