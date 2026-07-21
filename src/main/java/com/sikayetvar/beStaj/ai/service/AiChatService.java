package com.sikayetvar.beStaj.ai.service;

/**
 * Sohbet tabanlı bir AI sağlayıcısı ile temel istek/cevap akışının sözleşmesini tanımlar.
 * Bu arayüz sayesinde çağıran taraflar (controller vb.) hangi sağlayıcının
 * (OpenAI, başka bir sağlayıcı vb.) kullanıldığını bilmek zorunda kalmaz;
 * sağlayıcı değişikliği yalnızca yeni bir implementasyon eklemeyi gerektirir.
 */
public interface AiChatService {

    /**
     * Verilen kullanıcı mesajını AI modeline iletir ve üretilen yanıtı döndürür.
     *
     * @param userMessage kullanıcının modele iletmek istediği mesaj
     * @return modelin ürettiği yanıt metni
     */
    String getChatReply(String userMessage);
}
