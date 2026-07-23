package com.sikayetvar.beStaj.book.repository;

import com.sikayetvar.beStaj.book.entity.Author;
import com.sikayetvar.beStaj.book.entity.Book;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {
    }

    public static Specification<Book> idEquals(Long id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> isbnEquals(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> publishedYearEquals(Integer publishedYear) {
        return (root, query, cb) -> cb.equal(root.get("publishedYear"), publishedYear);
    }

    public static Specification<Book> authorNameContains(String authorName) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Book, Author> authors = root.join("authors");
            return cb.like(cb.lower(authors.get("name")), "%" + authorName.toLowerCase() + "%");
        };
    }
}
