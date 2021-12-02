package com.johnreah.postgres.spring.repositories;

import com.johnreah.postgres.spring.entities.Line;
import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<Line, Integer> {
}
