package dev.eder.biblioteca.exception;

// 404: lo que buscas no existe

public class RecursoNoEncontradoException extends RuntimeException{
    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
