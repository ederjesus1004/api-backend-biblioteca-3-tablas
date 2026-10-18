package dev.eder.biblioteca.repository;

import dev.eder.biblioteca.entidades.EstadoPrestamo;
import dev.eder.biblioteca.entidades.Prestamo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // ¿el libro tiene préstamos? -> para no dejar borrarlo
    boolean existsByLibroId(String libroId);

    // ¿este lector ya tiene este mismo libro sin devolver?
    //existsByLibroIdAndLectorDniAndEstado: se lee de izquierda a derecha. "¿Existe un préstamo de este libro,
    // para este lector, en este estado?". Cada And agrega una condición al WHERE.
    boolean existsByLibroIdAndLectorDniAndEstado(Long libroId, String lectorDni, EstadoPrestamo estado);

    // ¿cuántos libros tiene el lector sin devolver?
    //countByLectorDniAndEstado devuelve long: cuántos préstamos ACTIVOS tiene el lector. Con eso aplicamos el máximo de 3.
    boolean countByLectorDniAndEstado(String lectorDni, EstadoPrestamo estado);

    // @EntityGraph trae el libro en la MISMA consulta (evita el problema N+1)
    @Override
    @EntityGraph(attributePaths = "libro")
    List<Prestamo> findAll();

    @EntityGraph(attributePaths = "libro")
    List<Prestamo> findByLectorDni(String lectorDni);
}
