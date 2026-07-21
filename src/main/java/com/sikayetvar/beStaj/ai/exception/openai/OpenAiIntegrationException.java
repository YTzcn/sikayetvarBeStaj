package com.sikayetvar.beStaj.ai.exception.openai;

/**
 * OpenAI API ile iletişim sırasında oluşan hataları temsil eder.
 */
public class OpenAiIntegrationException extends RuntimeException {

    public OpenAiIntegrationException(String message) {
        super(message);
    }

    public OpenAiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
