package com.johnreah.postgres.spring;

import com.johnreah.postgres.spring.entities.Book;
import com.johnreah.postgres.spring.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    BookService bookService;

    @BeforeEach
    public void beforeEach() {
        bookService.deleteAll();
    }

    @Test
    public void givenUniqueConstraint_whenDupesSavedSeparately_thenNoRollback() {
        assertEquals(0, bookService.count(), "Table should start empty");

        assertThrows(DataIntegrityViolationException.class, () -> {
            Book book1 = new Book("Book One");
            Book book2 = new Book("Book One");
            bookService.addBook(book1);
            assertEquals(1, bookService.count(), "Table should have 1 row after first insert");
            bookService.addBook(book2);
            fail("should never get here");
        }, "expect exception on save");

        assertEquals(1, bookService.count(), "Table should have 1 book after rollback");
    }

    @Test
    public void givenUniqueConstraint_whenDupesSavedWithinTransaction_thenRollback() {
        assertEquals(0, bookService.count(), "Table should start empty");

        assertThrows(DataIntegrityViolationException.class, () -> {
            Book book1 = new Book("Book One");
            Book book2 = new Book("Book One");
            bookService.addBooks(book1, book2);
            fail("should never get here");
        }, "expect exception on save");

        assertEquals(0, bookService.count(), "Table should have 0 books after rollback");
    }


}
