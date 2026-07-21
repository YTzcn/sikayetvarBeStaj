package com.sikayetvar.beStaj.ai.exception.openai;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * AI modülünde oluşan hataları anlamlı HTTP yanıtlarına çevirir.
 */
@RestControllerAdvice(basePackages = "com.sikayetvar.beStaj.ai")
public class OpenAiExceptionHandler {

    @ExceptionHandler(OpenAiIntegrationException.class)
    public ProblemDetail handleOpenAiIntegrationException(OpenAiIntegrationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, exception.getMessage());
        problemDetail.setTitle("OpenAI isteği başarısız oldu");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Geçersiz istek");
        return problemDetail;
    }
}
