package com.sikayetvar.beStaj.book.repository;

import com.sikayetvar.beStaj.book.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {
    }

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> publishedYearEquals(Integer publishedYear) {
        return (root, query, cb) -> cb.equal(root.get("publishedYear"), publishedYear);
    }
}
