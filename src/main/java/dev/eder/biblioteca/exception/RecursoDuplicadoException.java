package dev.eder.biblioteca.exception;

// 409: ya existe otro registro con ese dato único

public class RecursoDuplicadoException extends RuntimeException{
    public RecursoDuplicadoException(String mensaje){
        super(mensaje);
    }
}
