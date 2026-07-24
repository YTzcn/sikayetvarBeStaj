package com.sikayetvar.beStaj.book.dto;

import java.util.List;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Integer publishedYear,
        List<AuthorResponse> authors
) {
}
