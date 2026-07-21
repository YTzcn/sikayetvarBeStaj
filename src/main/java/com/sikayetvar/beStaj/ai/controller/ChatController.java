package com.sikayetvar.beStaj.ai.controller;

import com.sikayetvar.beStaj.ai.dto.ChatRequest;
import com.sikayetvar.beStaj.ai.dto.ChatResponse;
import com.sikayetvar.beStaj.ai.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI sağlayıcısı ile temel istek/cevap akışını HTTP üzerinden dışarıya açan controller.
 * Hangi sağlayıcının kullanıldığını bilmez; yalnızca {@link AiChatService} sözleşmesine bağlıdır.
 */
@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final AiChatService chatService;

    public ChatController(AiChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Kullanıcıdan gelen mesajı AI modeline iletir ve yanıtı döndürür.
     *
     * @param request istemciden gelen sohbet isteği
     * @return modelin ürettiği yanıtı içeren cevap gövdesi
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        if (request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("Mesaj alanı boş olamaz.");
        }
        String reply = chatService.getChatReply(request.message());
        return new ChatResponse(reply);
    }
}
