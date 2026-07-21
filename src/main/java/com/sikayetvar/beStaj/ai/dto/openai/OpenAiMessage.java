package com.sikayetvar.beStaj.ai.dto.openai;

/**
 * OpenAI Chat Completions API'sindeki tek bir mesajı temsil eder.
 *
 * @param role    mesajı gönderen tarafın rolü ("system", "user" veya "assistant")
 * @param content mesajın metin içeriği
 */
public record OpenAiMessage(String role, String content) {
}
