package com.johnreah.postgres.spring;

import com.johnreah.postgres.spring.entities.Line;
import com.johnreah.postgres.spring.entities.Order;
import com.johnreah.postgres.spring.repositories.LineRepository;
import com.johnreah.postgres.spring.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    LineRepository lineRepository;

    @BeforeEach
    public void beforeEach() {
        lineRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    public void givenValidOrderWithNoLines_whenSave_thenOK() {
        Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
        orderRepository.save(order);
        assertEquals(1, orderRepository.count(), "Should have saved a single order");
    }

    @Test
    public void givenValidOrderWithLines_whenSave_thenOK() {
        Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
        orderRepository.save(order);
        lineRepository.save(new Line(order, "prod01", "Product One", 1, 11.11));
        lineRepository.save(new Line(order, "prod02", "Product Two", 2, 22.22));

        assertEquals(1, orderRepository.count(), "Should have saved 1 order record");
        assertEquals(2, lineRepository.count(), "Should have saved 2 line records");

        Order savedOrder = orderRepository.findAll().iterator().next();
        assertTrue(savedOrder != null && savedOrder.getCustomer().equals("Joe Bloggs"), "Should be able to retrieve order");
        assertTrue(savedOrder != null && savedOrder.getLines() != null && savedOrder.getLines().size() == 2, "Retrieved order should have 2 lines");
    }

    @Test
    public void givenOrderWithBrokenLine_whenSaveWithoutTransaction_thenPartial() {
        Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
        orderRepository.save(order);
        lineRepository.save(new Line(order, "prod01", "Product One", 1, 11.11));
        Exception e = assertThrows(Exception.class, () -> {
            lineRepository.save(new Line(order, null, "Product Two", 2, 22.22));
        });
        System.out.println("Exception = " + e.getClass());

        assertEquals(1, orderRepository.count(), "Should have saved 1 order record");
        assertEquals(1, lineRepository.count(), "Should have saved 1 line record");

        Order savedOrder = orderRepository.findAll().iterator().next();
        assertTrue(savedOrder != null && savedOrder.getCustomer().equals("Joe Bloggs"), "Should be able to retrieve order");
        assertTrue(savedOrder != null && savedOrder.getLines() != null && savedOrder.getLines().size() == 1, "Retrieved broken order should have only 1 line");
    }

}