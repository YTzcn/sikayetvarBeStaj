package com.sikayetvar.beStaj.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class B4BookUpdateRequest {

    @NotBlank(message = "Kitap başlığı boş olamaz")
    private String title;

    private String isbn;

    private Integer publishedYear;

    @NotEmpty(message = "En az bir yazar belirtilmelidir")
    private List<@NotBlank(message = "Yazar adı boş olamaz") String> authorNames;
}
