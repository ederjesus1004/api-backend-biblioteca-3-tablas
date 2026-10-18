# API de Biblioteca

API REST para una biblioteca con 3 tablas relacionadas: categorías, libros y préstamos. Prestar un libro resta una copia del stock y devolverlo la suma.

## Tecnologías

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- PostgreSQL
- Lombok
- Maven

## Tablas

```
categorias 1 ──── N libros 1 ──── N prestamos
```

| Tabla | Campos |
|---|---|
| categorias | id, nombre (único), descripcion, fecha_registro |
| libros | id, isbn (único), titulo, autor, anio_publicacion, stock, categoria_id (FK), fecha_registro |
| prestamos | id, libro_id (FK), lector_dni, lector_nombre, fecha_prestamo, fecha_limite, fecha_devolucion, estado |

## Endpoints

### Categorías

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/categorias` | Lista las categorías | 200 |
| GET | `/api/categorias/{id}` | Busca una categoría | 200 / 404 |
| POST | `/api/categorias` | Crea una categoría | 201 / 400 / 409 |
| PUT | `/api/categorias/{id}` | Actualiza una categoría | 200 / 400 / 404 / 409 |
| DELETE | `/api/categorias/{id}` | Elimina una categoría sin libros | 204 / 404 / 409 |

### Libros

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/libros` | Lista los libros | 200 |
| GET | `/api/libros?categoriaId=1` | Lista los libros de una categoría | 200 |
| GET | `/api/libros/{id}` | Busca un libro | 200 / 404 |
| POST | `/api/libros` | Crea un libro | 201 / 400 / 404 / 409 |
| PUT | `/api/libros/{id}` | Actualiza un libro | 200 / 400 / 404 / 409 |
| DELETE | `/api/libros/{id}` | Elimina un libro sin préstamos | 204 / 404 / 409 |

### Préstamos

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/prestamos` | Lista los préstamos | 200 |
| GET | `/api/prestamos?lectorDni=71234567` | Lista los préstamos de un lector | 200 |
| GET | `/api/prestamos/{id}` | Busca un préstamo | 200 / 404 |
| POST | `/api/prestamos` | Presta un libro | 201 / 400 / 404 / 409 |
| PATCH | `/api/prestamos/{id}/devolver` | Devuelve un libro | 200 / 404 / 409 |

## Reglas de negocio

- El nombre de una categoría no se repite, sin importar mayúsculas ni espacios de más.
- El ISBN de un libro no se repite.
- Un libro necesita una categoría que exista.
- No se elimina una categoría que tiene libros.
- No se elimina un libro que tiene préstamos registrados, porque son el historial.
- Solo se presta un libro que tenga stock.
- Prestar resta 1 al stock y devolver suma 1.
- Un lector no puede tener el mismo libro dos veces sin devolver.
- Un lector puede tener como máximo 3 préstamos activos.
- Un préstamo ya devuelto no se puede devolver otra vez.

## Errores

Todos los errores responden con el mismo formato:

| Código | Cuándo |
|---|---|
| 400 | Datos que no cumplen las validaciones, o JSON mal escrito |
| 404 | La categoría, el libro o el préstamo no existe |
| 409 | Dato duplicado, o una regla de negocio no permite la operación |
| 500 | Error inesperado (el detalle queda en el log) |

```json
{
  "fecha": "2026-10-18T15:30:00",
  "estado": 400,
  "mensaje": "Datos inválidos",
  "errores": {
    "isbn": "El ISBN debe tener 13 dígitos",
    "stock": "El stock no puede ser negativo"
  }
}
```

## Estructura

```
src/main/java/dev/eder/biblioteca/
├── exception/      excepciones propias, ErrorResponse y GlobalExceptionHandler
├── entity/         Categoria, Libro, Prestamo y el enum EstadoPrestamo
├── repository/     consultas a la base de datos
├── dto/            Request (entrada) y Response (salida) de cada tabla
├── mapper/         convierte entre DTO y entidad
├── service/        interfaces e implementaciones con las reglas de negocio
└── controller/     endpoints REST
```

## Cómo correrlo

1. Crea la base de datos en PostgreSQL:

```sql
   CREATE DATABASE biblioteca_db;
```

2. Configura tu contraseña de PostgreSQL como variable de entorno. En IntelliJ: *Run → Edit Configurations → Environment variables*:

```
   DB_PASSWORD=tu_contraseña
```

   Si no la defines, el proyecto usa `postgres`.

3. Ejecuta el proyecto:

```bash
   ./mvnw spring-boot:run
```

La API queda en `http://localhost:8080` y las tablas se crean solas al arrancar.

## Ejemplo

**POST** `/api/libros`

```json
{
  "isbn": "9780307474728",
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "anioPublicacion": 1967,
  "stock": 2,
  "categoriaId": 1
}
```

**POST** `/api/prestamos`

```json
{
  "libroId": 1,
  "lectorDni": "71234567",
  "lectorNombre": "Ana Torres",
  "dias": 7
}
```

Respuesta **201 Created**:

```json
{
  "id": 1,
  "libroId": 1,
  "libroTitulo": "Cien años de soledad",
  "lectorDni": "71234567",
  "lectorNombre": "Ana Torres",
  "fechaPrestamo": "2026-10-18",
  "fechaLimite": "2026-10-25",
  "fechaDevolucion": null,
  "estado": "ACTIVO",
  "vencido": false
}
```

Después del préstamo, el stock del libro baja de 2 a 1.

## Autor

Eder Cuaresma · [GitHub](https://github.com/ederjesus1004)
