package com.sikayetvar.beStaj.book.repository;

import com.sikayetvar.beStaj.book.entity.Book;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findBooksPublishedAfter(Integer year);
}
