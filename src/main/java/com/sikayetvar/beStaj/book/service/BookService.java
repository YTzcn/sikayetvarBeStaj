package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.B1BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.B4BookUpdateRequest;

import java.util.List;

public interface BookService {

    BookResponse createBook(B1BookCreateRequest request);

    List<BookResponse> listBooks();

    List<BookResponse> searchBooksByTitle(String title);

    List<BookResponse> searchBooksByAuthor(String authorName);

    List<BookResponse> filterBooks(Long id, String title, String isbn, Integer publishedYear, String authorName);

    List<BookResponse> findBooksPublishedAfter(Integer year);

    BookResponse getBookById(Long id);

    BookResponse updateBook(Long id, B4BookUpdateRequest request);

    void deleteBook(Long id);
}
