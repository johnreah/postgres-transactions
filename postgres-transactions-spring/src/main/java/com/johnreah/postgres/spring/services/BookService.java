package com.johnreah.postgres.spring.services;

import com.johnreah.postgres.spring.entities.Book;
import com.johnreah.postgres.spring.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void addBooks(Book... books) {
        for (Book book: books) {
            bookRepository.save(book);
        }
    }

    public long count() {
        return bookRepository.count();
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
