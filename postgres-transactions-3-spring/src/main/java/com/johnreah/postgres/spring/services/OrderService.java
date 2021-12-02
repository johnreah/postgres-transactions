package com.johnreah.postgres.spring.services;

import com.johnreah.postgres.spring.repositories.LineRepository;
import com.johnreah.postgres.spring.repositories.OrderRepository;
import com.johnreah.postgres.spring.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    LineRepository lineRepository;

    public void addOrder(Order order) {
        orderRepository.save(order);
    }
}
