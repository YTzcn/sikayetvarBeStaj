package com.sikayetvar.beStaj.ai.service;

import com.sikayetvar.beStaj.ai.config.openai.OpenAiProperties;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiChatRequest;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiChatResponse;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiMessage;
import com.sikayetvar.beStaj.ai.exception.openai.OpenAiIntegrationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * {@link AiChatService} sözleşmesinin OpenAI Chat Completions API'sini kullanan implementasyonu.
 */
@Service
public class OpenAiChatService implements AiChatService {

    private static final String CHAT_COMPLETIONS_PATH = "/chat/completions";
    private static final String ROLE_USER = "user";

    private final RestClient openAiRestClient;
    private final OpenAiProperties properties;

    public OpenAiChatService(RestClient openAiRestClient, OpenAiProperties properties) {
        this.openAiRestClient = openAiRestClient;
        this.properties = properties;
    }

    /**
     * Verilen kullanıcı mesajını OpenAI modeline iletir ve üretilen yanıtı döndürür.
     *
     * @param userMessage kullanıcının modele iletmek istediği mesaj
     * @return modelin ürettiği yanıt metni
     * @throws OpenAiIntegrationException API anahtarı tanımlı değilse, istek başarısız olursa
     *                                     veya model boş yanıt dönerse fırlatılır
     */
    @Override
    public String getChatReply(String userMessage) {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) {
            throw new OpenAiIntegrationException(
                    "OPENAI_API_KEY tanımlı değil. Lütfen ortam değişkeni olarak ayarlayın.");
        }

        OpenAiChatRequest request = new OpenAiChatRequest(
                properties.model(),
                List.of(new OpenAiMessage(ROLE_USER, userMessage))
        );

        OpenAiChatResponse response = sendRequest(request);

        return extractReply(response);
    }

    private OpenAiChatResponse sendRequest(OpenAiChatRequest request) {
        try {
            return openAiRestClient.post()
                    .uri(CHAT_COMPLETIONS_PATH)
                    .body(request)
                    .retrieve()
                    .body(OpenAiChatResponse.class);
        } catch (RestClientException exception) {
            throw new OpenAiIntegrationException("OpenAI isteği gönderilirken bir hata oluştu.", exception);
        }
    }

    private String extractReply(OpenAiChatResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new OpenAiIntegrationException("OpenAI boş yanıt döndürdü.");
        }
        return response.choices().getFirst().message().content();
    }
}
