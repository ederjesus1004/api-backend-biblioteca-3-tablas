package dev.eder.biblioteca.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor

public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(name = "ano_publicado", nullable = false)
    private Integer anoPublicado;

    // copias que quedan en el estante para prestar
    @Column(nullable = false)
    private Integer stock;

    // muchos libros pertenecen a una categoría: la FK categoria_id vive en esta tabla
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //Relación muchos-a-uno bajo demanda.
    //fetch = FetchType.LAZY: la categoría no se carga hasta que la usas. Por defecto ManyToOne es EAGER; ponerlo LAZY es la buena práctica.
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    void alCrear(){
        this.fechaRegistro = LocalDateTime.now();
    }
}
