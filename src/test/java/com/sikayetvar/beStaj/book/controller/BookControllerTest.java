package com.sikayetvar.beStaj.book.controller;

import com.sikayetvar.beStaj.book.dto.AuthorResponse;
import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.BookUpdateRequest;
import com.sikayetvar.beStaj.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    private BookController controller;

    @BeforeEach
    void setUp() {
        controller = new BookController(bookService);
    }

    @Test
    void createBook_delegatesToServiceAndReturnsResponse() {
        BookCreateRequest request = new BookCreateRequest("1984", "978-0451524935", 1949, List.of("George Orwell"));
        BookResponse expected = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.createBook(request)).thenReturn(expected);

        ResponseEntity<BookResponse> actual = controller.createBook(request);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getBody()).isEqualTo(expected);
    }

    @Test
    void listBooks_returnsOkWithAllBooksFromService() {
        BookResponse book = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.listBooks()).thenReturn(List.of(book));

        ResponseEntity<List<BookResponse>> actual = controller.listBooks();

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).containsExactly(book);
    }

    @Test
    void listBooks_returnsNoContentWhenEmpty() {
        when(bookService.listBooks()).thenReturn(List.of());

        ResponseEntity<List<BookResponse>> actual = controller.listBooks();

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actual.getBody()).isNull();
    }

    @Test
    void getBook_delegatesToServiceAndReturnsResponse() {
        BookResponse expected = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.getBookById(1L)).thenReturn(expected);

        BookResponse actual = controller.getBook(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateBook_delegatesToServiceAndReturnsResponse() {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("1984");
        request.setIsbn("978-0451524935");
        request.setPublishedYear(1949);
        request.setAuthorNames(List.of("George Orwell"));
        BookResponse expected = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.updateBook(1L, request)).thenReturn(expected);

        BookResponse actual = controller.updateBook(1L, request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteBook_delegatesToService() {
        controller.deleteBook(1L);

        verify(bookService).deleteBook(1L);
    }
}
