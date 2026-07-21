package com.sikayetvar.beStaj.book.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("ID'si " + id + " olan kitap bulunamadı.");
    }
}
