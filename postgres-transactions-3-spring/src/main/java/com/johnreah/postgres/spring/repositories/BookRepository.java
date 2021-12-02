package com.johnreah.postgres.spring.repositories;

import com.johnreah.postgres.spring.entities.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
