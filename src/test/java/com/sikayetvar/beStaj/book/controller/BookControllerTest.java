package com.sikayetvar.beStaj.book.controller;

import com.sikayetvar.beStaj.book.dto.AuthorResponse;
import com.sikayetvar.beStaj.book.dto.BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Test
    void createBook_delegatesToServiceAndReturnsResponse() {
        BookController controller = new BookController(bookService);
        BookCreateRequest request = new BookCreateRequest("1984", "978-0451524935", 1949, List.of("George Orwell"));
        BookResponse expected = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.createBook(request)).thenReturn(expected);

        BookResponse actual = controller.createBook(request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void listBooks_returnsAllBooksFromService() {
        BookController controller = new BookController(bookService);
        BookResponse book = new BookResponse(1L, "1984", "978-0451524935", 1949,
                List.of(new AuthorResponse(1L, "George Orwell")));
        when(bookService.listBooks()).thenReturn(List.of(book));

        List<BookResponse> actual = controller.listBooks();

        assertThat(actual).containsExactly(book);
    }
}
