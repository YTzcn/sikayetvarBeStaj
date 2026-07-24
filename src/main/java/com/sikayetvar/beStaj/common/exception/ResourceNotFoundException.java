package com.sikayetvar.beStaj.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super("ID'si " + id + " olan " + resourceName + " bulunamadı.");
    }
}
