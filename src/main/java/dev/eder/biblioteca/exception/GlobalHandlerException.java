package dev.eder.biblioteca.exception;

//Atrapa las excepciones de toda la API y responde con ErrorResponse.
// Tu receta: 404, 409, 409, 400, 400, 500 y el método armarRespuesta

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


//armarRespuesta es private: es un ayudante que solo usa esta clase, para no repetir las mismas dos líneas en cada método.
//@ExceptionHandler = Este método se ejecuta cuando ocurre la excepción indicada.
@Slf4j   //Crea el atributo log para escribir en la consola con niveles (info, warn, error). LOMBOK
@RestControllerAdvice   //Esta clase atiende las excepciones de todos los controllers y responde en JSON.
public class GlobalHandlerException {

    // ---> CORRESPONDE AL ERROR 404 <---
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex){
        return armarRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
    }

    // ---> CORRESPONDE AL PRIMER ERROR 409 <---
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(RecursoDuplicadoException ex){
        return armarRespuesta(HttpStatus.CONFLICT, ex.getMessage(), Map.of());
    }

    // ---> CORRESPONDE AL SEGUNDO ERROR 409 <---
    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ErrorResponse> manejarNoPermitida(OperacionNoPermitidaException ex){
        return armarRespuesta(HttpStatus.CONFLICT, ex.getMessage(), Map.of());
    }

    // ---> CORRESPONDE AL PRIMER ERROR 400 <---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex){
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return armarRespuesta(HttpStatus.BAD_REQUEST, "Datos invalidos", Map.of());
    }
    // ---> CORRESPONDE AL SEGUNDO ERROR 400 <---
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex){
        return armarRespuesta(HttpStatus.BAD_REQUEST, "El JSON enviado no es valido", Map.of());
    }

    // ---> CORRESPONDE AL ERROR 500 <---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex){
        log.error("Error inseperado", ex);
        return armarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado", Map.of());
    }


    //armarRespuesta es private: es un ayudante que solo usa esta clase, para no repetir las mismas dos líneas en cada método.
    private ResponseEntity<ErrorResponse> armarRespuesta(HttpStatus status, String mensaje, Map<String , String> errores){
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), mensaje, errores);
        return ResponseEntity.status(status).body(body);
    }
}
