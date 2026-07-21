package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import com.sikayetvar.beStaj.book.exception.BookNotFoundException;
import com.sikayetvar.beStaj.book.repository.AuthorRepository;
import com.sikayetvar.beStaj.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, authorRepository);
    }

    @Test
    void createBook_reusesExistingAuthorByName() {
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
        Book book = new Book("Kürk Mantolu Madonna", "978-0000000002", 1943);
        book.addAuthor(new Author("Sabahattin Ali"));

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.listBooks();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("Kürk Mantolu Madonna");
    }

    @Test
    void getBookById_returnsMatchingBook() {
        Book book = new Book("Simyacı", "978-0000000003", 1988);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertThat(response.title()).isEqualTo("Simyacı");
    }

    @Test
    void getBookById_throwsWhenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void updateBook_replacesFieldsAndAuthors() {
        Book book = new Book("Eski Başlık", "978-0000000004", 2000);
        book.addAuthor(new Author("Eski Yazar"));
        BookCreateRequest request = new BookCreateRequest(
                "Yeni Başlık", "978-0000000005", 2010, List.of("Yeni Yazar"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findByNameIgnoreCase("Yeni Yazar")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.updateBook(1L, request);

        assertThat(response.title()).isEqualTo("Yeni Başlık");
        assertThat(response.isbn()).isEqualTo("978-0000000005");
        assertThat(response.authors()).extracting("name").containsExactly("Yeni Yazar");
    }

    @Test
    void updateBook_throwsWhenNotFound() {
        BookCreateRequest request = new BookCreateRequest("Başlık", null, null, List.of("Yazar"));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(99L, request))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void deleteBook_removesExistingBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_throwsWhenNotFound() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(BookNotFoundException.class);
    }
}
