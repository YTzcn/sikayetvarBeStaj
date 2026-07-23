package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.BookUpdateRequest;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookCreateRequest request);

    List<BookResponse> listBooks();

    List<BookResponse> searchBooksByTitle(String title);

    List<BookResponse> searchBooksByAuthor(String authorName);

    List<BookResponse> filterBooks(String title, Integer publishedYear);

    List<BookResponse> findBooksPublishedAfter(Integer year);

    BookResponse getBookById(Long id);

    BookResponse updateBook(Long id, BookUpdateRequest request);

    void deleteBook(Long id);
}
