package dev.eder.biblioteca.service;

import dev.eder.biblioteca.dto.LibroRequest;
import dev.eder.biblioteca.dto.LibroResponse;
import dev.eder.biblioteca.entidades.Categoria;
import dev.eder.biblioteca.entidades.Libro;
import dev.eder.biblioteca.exception.OperacionNoPermitidaException;
import dev.eder.biblioteca.exception.RecursoDuplicadoException;
import dev.eder.biblioteca.exception.RecursoNoEncontradoException;
import dev.eder.biblioteca.mapper.LibroMapper;
import dev.eder.biblioteca.repository.CategoriaRepository;
import dev.eder.biblioteca.repository.LibroRepository;
import dev.eder.biblioteca.repository.PrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;   // para buscar el PADRE
    private final PrestamoRepository prestamoRepository;     // para saber si tiene HIJOS

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listar(Long categoriaId) {
        List<Libro> libros = (categoriaId == null)
                ? libroRepository.findAll()
                : libroRepository.findByCategoriaId(categoriaId);
        return libros.stream()
                .map(LibroMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse obtenerPorId(Long id) {
        return LibroMapper.toResponse(buscarLibro(id));
    }

    @Override
    @Transactional
    public LibroResponse crear(LibroRequest request) {
        // 1. Normalizar
        String isbn = request.isbn().trim();

        // 2. Reglas
        if (libroRepository.existsByIsbn(isbn)) {
            throw new RecursoDuplicadoException("Ya existe un libro con el ISBN " + isbn);
        }

        // 3. Buscar el padre (404 si no existe)
        Categoria categoria = buscarCategoria(request.categoriaId());

        // 4. Mapear
        Libro libro = LibroMapper.toEntity(request, categoria);

        // 5. Guardar y responder
        return LibroMapper.toResponse(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public LibroResponse actualizar(Long id, LibroRequest request) {
        // 0. Buscar
        Libro libro = buscarLibro(id);

        // 1. Normalizar
        String isbn = request.isbn().trim();

        // 2. Reglas: contra OTRO libro
        if (libroRepository.existsByIsbnAndIdNot(isbn, id)) {
            throw new RecursoDuplicadoException("Ya existe un libro con el ISBN " + isbn);
        }

        // 3. Buscar el padre (puede cambiar de categoría)
        Categoria categoria = buscarCategoria(request.categoriaId());

        // 4. Mapear sobre el que ya existe
        LibroMapper.actualizarEntidad(request, libro, categoria);

        // 5. Guardar y responder
        return LibroMapper.toResponse(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Libro libro = buscarLibro(id);

        // un libro con préstamos (aunque ya devueltos) se queda: es el historial
        if (prestamoRepository.existsByLibroId(id)) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar el libro " + libro.getTitulo() + " porque tiene préstamos registrados");
        }
        libroRepository.delete(libro);
    }

    private Libro buscarLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro con id " + id));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + id));
    }
}
