package com.sikayetvar.beStaj.book.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class BookExceptionHandlerTest {

    private final BookExceptionHandler handler = new BookExceptionHandler();

    @Test
    void handleDataIntegrityViolationException_returnsConflict() {
        ProblemDetail problemDetail = handler.handleDataIntegrityViolationException(
                new DataIntegrityViolationException("duplicate isbn"));

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(problemDetail.getDetail()).isEqualTo("Bu ISBN ile kayıtlı bir kitap zaten var.");
    }

    @Test
    void handleDuplicateIsbnException_returnsConflict() {
        ProblemDetail problemDetail = handler.handleDuplicateIsbnException(
                new DuplicateIsbnException("978-0000000001"));

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(problemDetail.getDetail()).isEqualTo("Bu ISBN ile kayıtlı bir kitap zaten var: 978-0000000001");
    }
}
