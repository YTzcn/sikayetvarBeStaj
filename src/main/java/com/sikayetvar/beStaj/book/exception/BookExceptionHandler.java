package com.sikayetvar.beStaj.book.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Book modülünde oluşan hataları anlamlı HTTP yanıtlarına çevirir.
 */
@RestControllerAdvice(basePackages = "com.sikayetvar.beStaj.book")
public class BookExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Bu ISBN ile kayıtlı bir kitap zaten var.");
        problemDetail.setTitle("Kitap kaydedilemedi");
        return problemDetail;
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ProblemDetail handleDuplicateIsbnException(DuplicateIsbnException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("Kitap kaydedilemedi");
        return problemDetail;
    }
}
