package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.AuthorResponse;
import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.BookUpdateRequest;
import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import com.sikayetvar.beStaj.book.exception.DuplicateIsbnException;
import com.sikayetvar.beStaj.book.repository.AuthorRepository;
import com.sikayetvar.beStaj.book.repository.BookRepository;
import com.sikayetvar.beStaj.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        requireIsbnAvailable(request.isbn(), null);

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

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        return toResponse(findBookOrThrow(id));
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = findBookOrThrow(id);
        requireIsbnAvailable(request.getIsbn(), id);
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.getAuthors().clear();
        request.getAuthorNames().forEach(name -> book.addAuthor(resolveAuthor(name)));
        return toResponse(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("kitap", id);
        }
        bookRepository.deleteById(id);
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("kitap", id));
    }

    private void requireIsbnAvailable(String isbn, Long currentBookId) {
        if (isbn == null) {
            return;
        }
        bookRepository.findByIsbn(isbn)
                .filter(existing -> currentBookId == null || !Objects.equals(existing.getId(), currentBookId))
                .ifPresent(existing -> {
                    throw new DuplicateIsbnException(isbn);
                });
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
