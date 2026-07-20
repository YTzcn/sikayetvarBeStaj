package com.sikayetvar.beStaj.ai.dto;

/**
 * İstemciden gelen sohbet isteğini temsil eder.
 *
 * @param message kullanıcının modele iletmek istediği mesaj metni
 */
public record ChatRequest(String message) {
}
