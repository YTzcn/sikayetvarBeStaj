package com.sikayetvar.beStaj.book.exception;

public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("Bu ISBN ile kayıtlı bir kitap zaten var: " + isbn);
    }
}
