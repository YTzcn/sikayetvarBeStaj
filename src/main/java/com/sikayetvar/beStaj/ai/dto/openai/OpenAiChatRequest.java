package com.sikayetvar.beStaj.ai.dto.openai;

import java.util.List;

/**
 * OpenAI Chat Completions API'sine gönderilecek istek gövdesi.
 *
 * @param model    kullanılacak model adı (örneğin "gpt-4o-mini")
 * @param messages konuşma geçmişini oluşturan mesaj listesi
 */
public record OpenAiChatRequest(String model, List<OpenAiMessage> messages) {
}
