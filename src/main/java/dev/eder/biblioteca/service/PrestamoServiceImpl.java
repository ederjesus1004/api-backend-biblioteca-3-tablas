package dev.eder.biblioteca.service;

import dev.eder.biblioteca.dto.PrestamoRequest;
import dev.eder.biblioteca.dto.PrestamoResponse;
import dev.eder.biblioteca.entidades.EstadoPrestamo;
import dev.eder.biblioteca.entidades.Libro;
import dev.eder.biblioteca.entidades.Prestamo;
import dev.eder.biblioteca.exception.OperacionNoPermitidaException;
import dev.eder.biblioteca.exception.RecursoNoEncontradoException;
import dev.eder.biblioteca.mapper.PrestamoMapper;
import dev.eder.biblioteca.repository.LibroRepository;
import dev.eder.biblioteca.repository.PrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PrestamoServiceImpl implements PrestamoService{
    private static final int MAX_PRESTAMOS_ACTIVOS = 3;

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;   // para buscar el PADRE y mover su stock

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar(String lectorDni) {
        List<Prestamo> prestamos = (lectorDni == null)
                ? prestamoRepository.findAll()
                : prestamoRepository.findByLectorDni(lectorDni.trim());
        return prestamos.stream()
                .map(PrestamoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponse obtenerPorId(Long id) {
        return PrestamoMapper.toResponse(buscarPrestamo(id));
    }

    @Override
    @Transactional
    public PrestamoResponse crear(PrestamoRequest request) {
        // 1. Normalizar
        String dni = request.lectorDni().trim();

        // 2. Buscar el padre (404 si no existe)
        Libro libro = buscarLibro(request.libroId());

        // 3. Reglas de negocio
        if (libro.getStock() <= 0) {
            throw new OperacionNoPermitidaException("No quedan copias de " + libro.getTitulo());
        }
        if (prestamoRepository.existsByLibroIdAndLectorDniAndEstado(libro.getId(), dni, EstadoPrestamo.ACTIVO)) {
            throw new OperacionNoPermitidaException("El lector " + dni + " ya tiene este libro sin devolver");
        }
        if (prestamoRepository.countByLectorDniAndEstado(dni, EstadoPrestamo.ACTIVO) >= MAX_PRESTAMOS_ACTIVOS) {
            throw new OperacionNoPermitidaException(
                    "El lector " + dni + " ya tiene " + MAX_PRESTAMOS_ACTIVOS + " libros sin devolver");
        }

        // 4. Mapear
        Prestamo prestamo = PrestamoMapper.toEntity(request, libro);

        // 5. Efecto en el padre: sale una copia del estante
        libro.setStock(libro.getStock() - 1);

        // 6. Guardar y responder (el libro se actualiza solo al terminar la transacción)
        return PrestamoMapper.toResponse(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public PrestamoResponse devolver(Long id) {
        // 0. Buscar
        Prestamo prestamo = buscarPrestamo(id);

        // 1. Reglas
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new OperacionNoPermitidaException("El préstamo " + id + " ya fue devuelto");
        }

        // 2. Cambiar estado
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucion(LocalDate.now());

        // 3. Efecto en el padre: la copia vuelve al estante
        Libro libro = prestamo.getLibro();
        libro.setStock(libro.getStock() + 1);

        // 4. Guardar y responder
        return PrestamoMapper.toResponse(prestamoRepository.save(prestamo));
    }

    private Prestamo buscarPrestamo(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el préstamo con id " + id));
    }

    private Libro buscarLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro con id " + id));
    }
}
