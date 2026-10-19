package com.cibertec.urbix.exception;

public class YaExisteException extends RuntimeException {

    public YaExisteException(String codigo) {
        super("La incidencia " + codigo + " ya fue sincronizada.");
    }
}