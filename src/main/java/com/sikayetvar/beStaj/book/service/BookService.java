package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookCreateRequest request);

    List<BookResponse> listBooks();

    BookResponse getBookById(Long id);

    BookResponse updateBook(Long id, BookCreateRequest request);

    void deleteBook(Long id);
}
