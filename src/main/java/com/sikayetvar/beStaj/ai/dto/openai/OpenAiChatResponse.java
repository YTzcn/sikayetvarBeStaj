package com.sikayetvar.beStaj.ai.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * OpenAI Chat Completions API'sinden dönen yanıt gövdesi.
 * İhtiyaç duyulmayan alanlar bilinmeyen alan olarak yoksayılır.
 *
 * @param choices modelin ürettiği yanıt seçenekleri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiChatResponse(List<OpenAiChoice> choices) {
}
