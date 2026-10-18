package dev.eder.biblioteca.service;

import dev.eder.biblioteca.dto.PrestamoRequest;
import dev.eder.biblioteca.dto.PrestamoResponse;

import java.util.List;

//No es un CRUD. No hay actualizar ni eliminar: hay devolver.
public interface PrestamoService {

    // lectorDni puede ser null: entonces lista todos
    List<PrestamoResponse> listar(String lectorDni);

    PrestamoResponse obtenerPorId(Long id);

    PrestamoResponse crear(PrestamoRequest request);

    PrestamoResponse devolver(Long id);

}
