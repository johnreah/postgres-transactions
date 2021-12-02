package com.johnreah.postgres.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.TableUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ORMLiteIntegrationTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres_transactions_ormlite";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private static JdbcPooledConnectionSource connectionSource;

    private static Dao<OrderEntity, Long> orderDao;
    private static Dao<LineEntity, Long> lineDao;

    @BeforeAll
    public static void beforeAll() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connectionSource = new JdbcPooledConnectionSource(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        TableUtils.createTableIfNotExists(connectionSource, OrderEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, LineEntity.class);

        orderDao = DaoManager.createDao(connectionSource, OrderEntity.class);
        lineDao = DaoManager.createDao(connectionSource, LineEntity.class);
    }

    @AfterAll
    public static void afterAll() throws SQLException, IOException {
        TableUtils.dropTable(connectionSource, LineEntity.class, false);
        TableUtils.dropTable(connectionSource, OrderEntity.class, false);
        connectionSource.close();
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        orderDao.deleteBuilder().delete();
    }

    @Test
    public void givenEmptyTable_whenCreateGoodOrderWithNoLinesAndNoTransaction_thenOK() throws SQLException {
        OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), "Joe Bloggs");
        orderDao.create(orderEntity);
        OrderEntity result = orderDao.queryForId(orderEntity.getId());
        assertTrue(result != null && result.getId() == orderEntity.getId() && result.getId() > 0, "Order should have persisted with ID returned");
        assertEquals(0, lineDao.countOf(), "Lines table should be empty");
        assertEquals(1, orderDao.countOf(), "Orders table should have 1 row");
    }

    @Test
    public void givenEmptyTable_whenCreateGoodOrderWithLinesAndNoTransaction_thenOK() throws SQLException {
        OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), "Joe Bloggs");
        orderDao.create(orderEntity);
        orderDao.refresh(orderEntity);
        LineEntity line1 = new LineEntity("prod001", "Product One", 1, 11.11);
        LineEntity line2 = new LineEntity("prod002", "Product Two", 2, 22.22);
        orderEntity.getLines().add(line1);
        orderEntity.getLines().add(line2);
        orderDao.createOrUpdate(orderEntity);

        OrderEntity result = orderDao.queryForId(orderEntity.getId());
        assertTrue(result != null && result.getId() == orderEntity.getId(), "Order should have persisted with ID returned");
        assertTrue(result.getLines() != null && result.getLines().size() == 2, "Order should have persisted with 2 lines");
        assertEquals(1, orderDao.countOf(), "Should be 1 order in order table");
        assertEquals(2, lineDao.countOf(), "Should be 2 lines in lines table");
    }

    @Test
    public void givenEmptyTable_whenCreateBadOrderWithNoTransaction_thenNoRollback() throws SQLException {
        OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), "Joe Bloggs");
        orderDao.create(orderEntity);
        orderDao.refresh(orderEntity);
        LineEntity line1 = new LineEntity("prod001", "Product One", 1, 11.11);
        LineEntity line2 = new LineEntity(null, "Product Two", 2, 22.22);
        orderEntity.getLines().add(line1);
        assertThrows(IllegalStateException.class, () -> {
            orderEntity.getLines().add(line2);
        });
        orderDao.createOrUpdate(orderEntity);

        OrderEntity savedOrderEntity = orderDao.queryForId(orderEntity.getId());
        assertTrue(savedOrderEntity != null && savedOrderEntity.getId() == orderEntity.getId(), "Order should have persisted with ID returned");
        assertEquals(1, orderDao.countOf(), "Should be 1 row in Orders table");
        assertEquals(1, lineDao.countOf(), "Should be 1 row in Lines table");
    }

    @Test
    public void givenEmptyTable_whenCreateBadOrderWithTransaction_thenRollback() throws SQLException {
        TransactionManager tm = new TransactionManager(connectionSource);
        try {
            assertEquals(0, orderDao.countOf(), "Orders table should be empty before first insert");
            assertEquals(0, lineDao.countOf(), "Lines table should be empty before first insert");
            tm.callInTransaction(() -> {
                OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), "Joe Bloggs");
                orderDao.create(orderEntity);
                orderDao.refresh(orderEntity);
                LineEntity line1 = new LineEntity("prod001", "Product One", 1, 11.11);
                LineEntity line2 = new LineEntity(null, "Product Two", 2, 22.22);
                orderEntity.getLines().add(line1);
                assertEquals(1, orderDao.countOf(), "Orders table should have 1 row before exception");
                assertEquals(1, lineDao.countOf(), "Lines table should have 1 row before exception");
                orderEntity.getLines().add(line2);
                orderDao.createOrUpdate(orderEntity);
                return null;
            });
            fail("We should never get here");
        } catch (Exception e) {
            assertTrue(e.getClass().equals(SQLException.class), "ORMLite should have thrown SQLException caused by not-null constraint");
            e.printStackTrace();
        }

        assertEquals(0, orderDao.countOf(), "Should be 0 rows in Orders table because transaction has cleaned up");
        assertEquals(0, lineDao.countOf(), "Should be 0 rows in Lines table because transaction has cleaned up");
    }

}