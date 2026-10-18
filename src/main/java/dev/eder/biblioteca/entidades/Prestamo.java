package dev.eder.biblioteca.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // muchos préstamos son de un libro: la FK libro_id vive en esta tabla
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "libros_id", nullable = false)
    private Libro libro;

    @Column(name = "lector_dni", nullable = false, length = 8)
    private String lectorDni;

    @Column(name = "lector_nombre", nullable = false, length = 120)
    private String lectorNombre;

    @Column(name = "fecha_prestamo", nullable = false, updatable = false)
    private LocalDate fechaPrestamo;

    @Column( name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    // queda vacía (null) hasta que el lector devuelve el libro
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING) //guarda el texto ACTIVO. Sin esto guarda un número (0, 1) y si cambias el orden del enum se rompen los datos.
    @Column(nullable = false, length = 10)
    private EstadoPrestamo estado;

    @PrePersist
    void alCrear(){
        this.fechaPrestamo = LocalDate.now();
        this.estado = EstadoPrestamo.ACTIVO;
    }
}
