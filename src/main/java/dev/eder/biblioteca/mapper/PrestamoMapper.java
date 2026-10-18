package dev.eder.biblioteca.mapper;

import dev.eder.biblioteca.dto.PrestamoRequest;
import dev.eder.biblioteca.dto.PrestamoResponse;
import dev.eder.biblioteca.entidades.EstadoPrestamo;
import dev.eder.biblioteca.entidades.Libro;
import dev.eder.biblioteca.entidades.Prestamo;

import java.time.LocalDate;

public class PrestamoMapper {
    private PrestamoMapper() {}

    // un préstamo no se edita (solo se devuelve), por eso no hay actualizarEntidad

    public static Prestamo toEntity(PrestamoRequest request, Libro libro) {
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setLectorDni(request.lectorDni().trim());
        prestamo.setLectorNombre(request.lectorNombre().trim());
        prestamo.setFechaLimite(LocalDate.now().plusDays(request.dias()));
        return prestamo;
    }

    public static PrestamoResponse toResponse(Prestamo prestamo){
        return PrestamoResponse.builder()
                .id(prestamo.getId())
                .libroId(prestamo.getLibro().getId())
                .libroTitulo(prestamo.getLibro().getTitulo())
                .lectorDni(prestamo.getLectorDni())
                .lectorNombre(prestamo.getLectorNombre())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaLimite(prestamo.getFechaLimite())
                .fechaDevolucion(prestamo.getFechaDevolucion())
                .estado(prestamo.getEstado())
                .vencido(estaVencido(prestamo))
                .build();
    }

    // vencido = sigue ACTIVO y ya pasó la fecha límite
    private static boolean estaVencido(Prestamo prestamo) {
        return prestamo.getEstado() == EstadoPrestamo.ACTIVO
                && LocalDate.now().isAfter(prestamo.getFechaLimite());
    }
}
