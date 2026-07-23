package com.sikayetvar.beStaj.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record B1BookCreateRequest(
        @NotBlank(message = "Kitap başlığı boş olamaz")
        String title,

        String isbn,

        Integer publishedYear,

        @NotEmpty(message = "En az bir yazar belirtilmelidir")
        List<@NotBlank(message = "Yazar adı boş olamaz") String> authorNames
) {
}
