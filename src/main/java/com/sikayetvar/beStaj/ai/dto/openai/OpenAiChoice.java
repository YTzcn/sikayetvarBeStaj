package com.sikayetvar.beStaj.ai.dto.openai;

/**
 * OpenAI yanıtındaki tek bir seçeneği (choice) temsil eder.
 *
 * @param message modelin ürettiği mesaj
 */
public record OpenAiChoice(OpenAiMessage message) {
}
