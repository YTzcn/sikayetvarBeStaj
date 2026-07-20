package com.sikayetvar.beStaj.ai.config.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenAI API bağlantısı için gerekli yapılandırma değerlerini taşır.
 * Değerler application.properties üzerinden "openai." ön ekiyle okunur.
 *
 * @param baseUrl        OpenAI API'sinin taban adresi
 * @param apiKey         kimlik doğrulama için kullanılan API anahtarı
 * @param model          istekte kullanılacak model adı
 * @param timeoutSeconds bağlantı ve okuma işlemleri için zaman aşımı süresi
 */
@ConfigurationProperties(prefix = "openai")
public record OpenAiProperties(
        String baseUrl,
        String apiKey,
        String model,
        int timeoutSeconds
) {
}
