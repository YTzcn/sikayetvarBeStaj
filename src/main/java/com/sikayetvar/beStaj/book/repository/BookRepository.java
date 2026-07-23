package com.sikayetvar.beStaj.book.repository;

import com.sikayetvar.beStaj.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>,
        BookRepositoryCustom {

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitle(@Param("title") String title);

    @Query(
            value = """
                    SELECT DISTINCT b.*
                    FROM books b
                    JOIN book_authors ba ON ba.book_id = b.id
                    JOIN authors a ON a.id = ba.author_id
                    WHERE LOWER(a.name) = LOWER(:authorName)
                    """,
            nativeQuery = true
    )
    List<Book> findByAuthorNameNative(@Param("authorName") String authorName);
}
