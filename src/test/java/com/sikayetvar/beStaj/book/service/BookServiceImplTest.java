package com.sikayetvar.beStaj.book.service;

import com.sikayetvar.beStaj.book.dto.B1BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.B4BookUpdateRequest;
import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import com.sikayetvar.beStaj.book.exception.DuplicateIsbnException;
import com.sikayetvar.beStaj.book.repository.AuthorRepository;
import com.sikayetvar.beStaj.book.repository.BookRepository;
import com.sikayetvar.beStaj.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

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
        B1BookCreateRequest request = new B1BookCreateRequest(
                "İnce Memed", "978-0000000001", 1955, List.of("Yaşar Kemal"));

        when(bookRepository.findByIsbn("978-0000000001")).thenReturn(Optional.empty());
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
        B1BookCreateRequest request = new B1BookCreateRequest(
                "Yeni Kitap", null, null, List.of("Yeni Yazar"));

        when(authorRepository.findByNameIgnoreCase("Yeni Yazar")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.createBook(request);

        assertThat(response.authors()).extracting("name").containsExactly("Yeni Yazar");
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void createBook_throwsWhenIsbnAlreadyExists() {
        B1BookCreateRequest request = new B1BookCreateRequest(
                "İkinci Kopya", "978-0000000001", 2020, List.of("Yazar"));

        when(bookRepository.findByIsbn("978-0000000001"))
                .thenReturn(Optional.of(new Book("İlk Kopya", "978-0000000001", 2019)));

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(DuplicateIsbnException.class);

        verify(bookRepository, never()).save(any(Book.class));
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
    void searchBooksByTitle_mapsMatchingBooks() {
        Book book = new Book("Kürk Mantolu Madonna", "978-0000000002", 1943);
        when(bookRepository.searchByTitle("mantolu")).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.searchBooksByTitle("mantolu");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("Kürk Mantolu Madonna");
    }

    @Test
    void searchBooksByAuthor_mapsMatchingBooks() {
        Book book = new Book("İnce Memed", "978-0000000008", 1955);
        when(bookRepository.findByAuthorNameNative("Yaşar Kemal")).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.searchBooksByAuthor("Yaşar Kemal");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("İnce Memed");
    }

    @Test
    void filterBooks_combinesTitleAndPublishedYearCriteria() {
        Book book = new Book("Simyacı", "978-0000000009", 1988);
        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any())).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.filterBooks(null, "simya", null, 1988, null);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("Simyacı");
    }

    @Test
    void filterBooks_appliesIdCriterion() {
        Book book = new Book("Simyacı", "978-0000000009", 1988);
        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any())).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.filterBooks(1L, null, null, null, null);

        assertThat(responses).hasSize(1);
    }

    @Test
    void filterBooks_appliesIsbnCriterion() {
        Book book = new Book("Simyacı", "978-0000000009", 1988);
        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any())).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.filterBooks(null, null, "978-0000000009", null, null);

        assertThat(responses).hasSize(1);
    }

    @Test
    void filterBooks_appliesAuthorNameCriterion() {
        Book book = new Book("Simyacı", "978-0000000009", 1988);
        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any())).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.filterBooks(null, null, null, null, "Paulo");

        assertThat(responses).hasSize(1);
    }

    @Test
    void filterBooks_returnsAllBooksWhenNoCriteriaGiven() {
        Book book = new Book("Simyacı", "978-0000000009", 1988);
        when(bookRepository.findAll((Specification<Book>) null)).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.filterBooks(null, null, null, null, null);

        assertThat(responses).hasSize(1);
    }

    @Test
    void findBooksPublishedAfter_mapsMatchingBooks() {
        Book book = new Book("Şeker Portakalı", "978-0000000010", 1968);
        when(bookRepository.findBooksPublishedAfter(1960)).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.findBooksPublishedAfter(1960);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().title()).isEqualTo("Şeker Portakalı");
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
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateBook_replacesFieldsAndAuthors() {
        Book book = new Book("Eski Başlık", "978-0000000004", 2000);
        book.addAuthor(new Author("Eski Yazar"));
        B4BookUpdateRequest request = updateRequest(
                "Yeni Başlık", "978-0000000005", 2010, List.of("Yeni Yazar"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn("978-0000000005")).thenReturn(Optional.empty());
        when(authorRepository.findByNameIgnoreCase("Yeni Yazar")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.updateBook(1L, request);

        assertThat(response.title()).isEqualTo("Yeni Başlık");
        assertThat(response.isbn()).isEqualTo("978-0000000005");
        assertThat(response.authors()).extracting("name").containsExactly("Yeni Yazar");
    }

    @Test
    void updateBook_throwsWhenNotFound() {
        B4BookUpdateRequest request = updateRequest("Başlık", null, null, List.of("Yazar"));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(99L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateBook_throwsWhenIsbnBelongsToAnotherBook() {
        Book book = new Book("Kitap", "978-0000000006", 2000);
        B4BookUpdateRequest request = updateRequest(
                "Kitap", "978-0000000007", 2000, List.of("Yazar"));
        Book otherBook = new Book("Başka Kitap", "978-0000000007", 2001);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn("978-0000000007")).thenReturn(Optional.of(otherBook));

        assertThatThrownBy(() -> bookService.updateBook(1L, request))
                .isInstanceOf(DuplicateIsbnException.class);
    }

    private static B4BookUpdateRequest updateRequest(String title, String isbn, Integer publishedYear,
                                                     List<String> authorNames) {
        B4BookUpdateRequest request = new B4BookUpdateRequest();
        request.setTitle(title);
        request.setIsbn(isbn);
        request.setPublishedYear(publishedYear);
        request.setAuthorNames(authorNames);
        return request;
    }

    @Test
    void deleteBook_marksBookAsDeletedWithoutPhysicalDeletion() {
        Book book = new Book("Kitap", "978-0000000011", 2000);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        assertThat(book.isDeleted()).isTrue();
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void deleteBook_throwsWhenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
