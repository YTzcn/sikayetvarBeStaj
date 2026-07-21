package com.sikayetvar.beStaj.ai.dto;

/**
 * Modelden alınan yanıtı istemciye döndürmek için kullanılır.
 *
 * @param reply modelin ürettiği yanıt metni
 */
public record ChatResponse(String reply) {
}
