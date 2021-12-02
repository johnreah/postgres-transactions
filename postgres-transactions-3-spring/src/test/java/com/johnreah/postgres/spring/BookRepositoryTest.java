package com.johnreah.postgres.spring;

import com.johnreah.postgres.spring.entities.Book;
import com.johnreah.postgres.spring.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void beforeEach() {
        bookRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Book book1 = new Book("Book One");
        Book book2 = new Book("Book Two");
        bookRepository.save(book1);
        bookRepository.save(book2);
        assertEquals(2, bookRepository.count(), "Should have 2 rows after 2 successful inserts");
    }

    @Test
    public void givenUniqueConstraint_whenDupeSaved_thenException() {
        assertEquals(0, bookRepository.count(), "Table should start empty");

        Book book1 = new Book("Book One");
        bookRepository.save(book1);
        assertEquals(1, bookRepository.count(), "Table should have 1 book");

        Book book2 = new Book("Book One");
        assertThrows(Exception.class, () -> bookRepository.save(book2), "expect exception on save");
        assertEquals(1, bookRepository.count(), "Table should still have 1 book after exception");
    }

}
