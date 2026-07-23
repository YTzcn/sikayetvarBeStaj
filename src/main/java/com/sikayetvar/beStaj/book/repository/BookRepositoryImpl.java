package com.sikayetvar.beStaj.book.repository;

import com.sikayetvar.beStaj.book.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findBooksPublishedAfter(Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        query.select(book).where(cb.greaterThan(book.get("publishedYear"), year));

        return entityManager.createQuery(query).getResultList();
    }
}
