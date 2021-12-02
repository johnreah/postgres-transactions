package com.johnreah.postgres.spring.repositories;

import com.johnreah.postgres.spring.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
