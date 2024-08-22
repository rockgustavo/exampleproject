package com.example.exampleproject.exception;

public class StatusInvalidoException extends RuntimeException {

    public StatusInvalidoException() {
        super("Status inválido!");
    }
}
