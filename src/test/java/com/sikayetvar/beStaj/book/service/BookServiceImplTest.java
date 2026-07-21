package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import com.sikayetvar.beStaj.book.repository.AuthorRepository;
import com.sikayetvar.beStaj.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private BookServiceImpl bookService;

    @Test
    void createBook_reusesExistingAuthorByName() {
        bookService = new BookServiceImpl(bookRepository, authorRepository);
        Author existingAuthor = new Author("Yaşar Kemal");
        BookCreateRequest request = new BookCreateRequest(
                "İnce Memed", "978-0000000001", 1955, List.of("Yaşar Kemal"));

        when(authorRepository.findByNameIgnoreCase("Yaşar Kemal")).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.createBook(request);

        assertThat(response.title()).isEqualTo("İnce Memed");
        assertThat(response.authors()).hasSize(1);
        assertThat(response.authors().getFirst().name()).isEqualTo("Yaşar Kemal");
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void createBook_createsNewAuthorWhenNotFound() {
        bookService = new BookServiceImpl(bookRepository, authorRepository);
        BookCreateRequest request = new BookCreateRequest(
                "Yeni Kitap", null, null, List.of("Yeni Yazar"));

        when(authorRepository.findByNameIgnoreCase("Yeni Yazar")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.createBook(request);

        assertThat(response.authors()).extracting("name").containsExactly("Yeni Yazar");
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void listBooks_mapsAllPersistedBooks() {
        bookService = new BookServiceImpl(bookRepository, authorRepository);
        Book book = new Book("Kürk Mantolu Madonna", "978-0000000002", 1943);
        book.addAuthor(new Author("Sabahattin Ali"));

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.listBooks();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("Kürk Mantolu Madonna");
    }
}
