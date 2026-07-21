package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.AuthorResponse;
import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import com.sikayetvar.beStaj.book.repository.AuthorRepository;
import com.sikayetvar.beStaj.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        Book book = new Book(request.title(), request.isbn(), request.publishedYear());
        request.authorNames().forEach(name -> book.addAuthor(resolveAuthor(name)));

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> listBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private Author resolveAuthor(String name) {
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(name)));
    }

    private BookResponse toResponse(Book book) {
        List<AuthorResponse> authors = book.getAuthors().stream()
                .map(author -> new AuthorResponse(author.getId(), author.getName()))
                .toList();
        return new BookResponse(book.getId(), book.getTitle(), book.getIsbn(), book.getPublishedYear(), authors);
    }
}
