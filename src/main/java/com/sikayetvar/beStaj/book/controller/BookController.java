package com.sikayetvar.beStaj.book.controller;

import com.sikayetvar.beStaj.book.dto.B1BookCreateRequest;
import com.sikayetvar.beStaj.book.dto.BookResponse;
import com.sikayetvar.beStaj.book.dto.B4BookUpdateRequest;
import com.sikayetvar.beStaj.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * B1: Yeni bir kitap kaydı oluşturur
     *
     * @param request Oluşturulacak kitabın bilgileri
     * @return Oluşturulan kitap, BookId header'ında kaydın id değeri ile birlikte
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody B1BookCreateRequest request) {
        BookResponse created = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("BookId", created.id().toString())
                .body(created);
    }

    /**
     * B2: Tüm kitapları listeler
     *
     * @return Kayıt varsa 200 ve kitap listesi, yoksa 204
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> listBooks() {
        List<BookResponse> books = bookService.listBooks();
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    /**
     * B6: Başlığa göre kitap arar (JPQL @Query)
     *
     * @param title Aranacak başlık (kısmi eşleşme)
     * @return Kayıt varsa 200 ve eşleşen kitaplar, yoksa 204
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String title) {
        List<BookResponse> books = bookService.searchBooksByTitle(title);
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    /**
     * B7: Yazar adına göre kitap arar (native @Query)
     *
     * @param name Aranacak yazar adı
     * @return Kayıt varsa 200 ve eşleşen kitaplar, yoksa 204
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> searchBooksByAuthor(@RequestParam String name) {
        List<BookResponse> books = bookService.searchBooksByAuthor(name);
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    /**
     * B8: Book alanlarının tamamına göre dinamik filtreleme yapar (Specification), hiçbiri zorunlu değildir
     *
     * @param id            Aranacak id (opsiyonel)
     * @param title         Aranacak başlık, kısmi eşleşme (opsiyonel)
     * @param isbn          Aranacak isbn (opsiyonel)
     * @param publishedYear Aranacak yayın yılı (opsiyonel)
     * @param authorName    Aranacak yazar adı, kısmi eşleşme (opsiyonel)
     * @return Kayıt varsa 200 ve eşleşen kitaplar, yoksa 204
     */
    @GetMapping("/filter")
    public ResponseEntity<List<BookResponse>> filterBooks(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer publishedYear,
            @RequestParam(required = false) String authorName) {
        List<BookResponse> books = bookService.filterBooks(id, title, isbn, publishedYear, authorName);
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    /**
     * B9: Belirtilen yıldan sonra yayınlanan kitapları arar (CriteriaBuilder)
     *
     * @param year Bu yıldan sonra yayınlanan kitaplar aranır
     * @return Kayıt varsa 200 ve eşleşen kitaplar, yoksa 204
     */
    @GetMapping("/search/published-after")
    public ResponseEntity<List<BookResponse>> booksPublishedAfter(@RequestParam Integer year) {
        List<BookResponse> books = bookService.findBooksPublishedAfter(year);
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    /**
     * B3: Verilen id değerine sahip kitabı getirir
     *
     * @param id Getirilecek kitabın id değeri
     * @return Kitap detayı
     */
    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    /**
     * B4: Verilen id değerine sahip kitabı günceller
     *
     * @param id      Güncellenecek kitabın id değeri
     * @param request Güncel kitap bilgileri
     * @return Güncellenmiş kitap
     */
    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @Valid @RequestBody B4BookUpdateRequest request) {
        return bookService.updateBook(id, request);
    }

    /**
     * B5: Verilen id değerine sahip kitabı siler (soft delete, kayıt fiziksel olarak silinmez)
     *
     * @param id Silinecek kitabın id değeri
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
