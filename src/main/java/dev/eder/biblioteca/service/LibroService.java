package dev.eder.biblioteca.service;


import dev.eder.biblioteca.dto.CategoriaResponse;
import dev.eder.biblioteca.dto.LibroRequest;
import dev.eder.biblioteca.dto.LibroResponse;

import java.util.List;

public interface LibroService {

    // categoriaId puede ser null: entonces lista todos
    List<LibroResponse> listar(Long categoriaId);

    LibroResponse obtenerPorId(Long id);

    LibroResponse crear(LibroRequest request);

    LibroResponse actualizar(Long id, LibroRequest request);

    void eliminar(Long id);
}
