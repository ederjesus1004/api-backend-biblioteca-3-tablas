package dev.eder.biblioteca.repository;

import dev.eder.biblioteca.entidades.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    // ¿la categoría tiene libros? -> para no dejar borrarla
    boolean existsByCategoriaId(Long categoriaId);

    // @EntityGraph trae la categoría en la MISMA consulta (evita el problema N+1)
    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Libro> findAll();

    @EntityGraph(attributePaths = "categoria")
    List<Libro> findByCategoriaId(Long categoriaId);
}
