package dev.eder.biblioteca.exception;

// 409: los datos están bien, pero la regla de negocio no deja hacerlo

public class OperacionNoPermitidaException extends RuntimeException{
    public OperacionNoPermitidaException(String mensaje){
        super(mensaje);
    }
}
