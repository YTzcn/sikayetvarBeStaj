package com.sikayetvar.beStaj.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class CommonExceptionHandlerTest {

    private final CommonExceptionHandler handler = new CommonExceptionHandler();

    @Test
    void handleResourceNotFoundException_returnsNotFound() {
        ProblemDetail problemDetail = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("kitap", 42L));

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(problemDetail.getDetail()).isEqualTo("ID'si 42 olan kitap bulunamadı.");
    }
}
