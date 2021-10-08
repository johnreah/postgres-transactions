package com.johnreah.postgres.ormlite_old;

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
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ORMLiteIntegrationTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/transactiontest";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private static JdbcPooledConnectionSource connectionSource;

    private static Dao<LibraryEntity, Long> libraryDao;
    //private static LibraryDao libraryDao;

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connectionSource = new JdbcPooledConnectionSource(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        TableUtils.createTableIfNotExists(connectionSource, LibraryEntity.class);

        //libraryDao = new LibraryDaoImpl(connectionSource, LibraryEntity.class);
        libraryDao = DaoManager.createDao(connectionSource, LibraryEntity.class);
    }

    @BeforeEach
    public void clearTables() throws SQLException {
        TableUtils.clearTable(connectionSource, LibraryEntity.class);
    }

    @Test
    public void givenDAO_when_CRUD_thenOK() throws SQLException {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setName("This is a library");
        libraryDao.create(libraryEntity);

        LibraryEntity result = libraryDao.queryForId(libraryEntity.getLibraryId());
        assertEquals("This is a library", result.getName(), "Names should match");
    }

    @Test
    public void givenNoTransaction_whenBadData_thenNoRollback() throws SQLException {
        //System.out.println(String.format("No rows inserted yet: count=%d", libraryDao.countOf()));
        assertEquals(0, libraryDao.countOf(), "Table should be empty before first insert");
        try {
            LibraryEntity library1 = new LibraryEntity();
            library1.setName("first");
            libraryDao.create(library1);
            //System.out.println(String.format("After first insert: count=%d", libraryDao.countOf()));
            assertEquals(1, libraryDao.countOf(), "Row count should be 1 after first insert");
            //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));

            LibraryEntity library2 = new LibraryEntity();
            library2.setName(null);
            libraryDao.create(library2);
            assertTrue(false, "We should never get here");
        } catch (SQLException e) {
            //System.out.println("In catch");
            assertEquals("23502", e.getSQLState(), "We should have failed on a not-null constraint");
            //e.printStackTrace();
        }
        //System.out.println("After try/catch");
        //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
        assertEquals(1, libraryDao.countOf(), "Row count should be 1 after failed insert without transaction");
    }

    @Test
    public void givenTransaction_whenGoodData_thenOK() throws SQLException {
        TransactionManager tm = new TransactionManager(connectionSource);
        try {
            //System.out.println(String.format("In try block before transaction: count=%d", libraryDao.countOf()));
            assertEquals(0, libraryDao.countOf(), "Table should be empty before first insert");
            tm.callInTransaction(() -> {
                LibraryEntity library1 = new LibraryEntity();
                library1.setName("first");
                libraryDao.create(library1);
                //System.out.println(String.format("In transaction after first insert: count=%d", libraryDao.countOf()));
                //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
                assertEquals(1, libraryDao.countOf(), "Row count should be 1 after first insert");

                LibraryEntity library2 = new LibraryEntity();
                library2.setName("second");
                libraryDao.create(library2);
                //System.out.println(String.format("In transaction after second insert: count=%d", libraryDao.countOf()));
                //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
                assertEquals(2, libraryDao.countOf(), "Row count should be 2 after second insert");
                return null;
            });
            //System.out.println(String.format("In try block after transaction: count=%d", libraryDao.countOf()));
            //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
            assertEquals(2, libraryDao.countOf(), "Row count should be 2 after transaction");
        } catch (Exception e) {
            //System.out.println("In catch");
            //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
            fail("We should not hit the catch block");
            e.printStackTrace();
        }
        //System.out.println("After try/catch");
        //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
        assertEquals(2, libraryDao.countOf(), "Row count should be 2 after transaction");
    }

    @Test
    public void givenTransaction_whenBadData_thenRollback() throws SQLException {
        TransactionManager tm = new TransactionManager(connectionSource);
        try {
            //System.out.println(String.format("In try block before transaction: count=%d", libraryDao.countOf()));
            assertEquals(0, libraryDao.countOf(), "Table should be empty before first insert");

            tm.callInTransaction(() -> {
                LibraryEntity library1 = new LibraryEntity();
                library1.setName("first");
                libraryDao.create(library1);
                //System.out.println(String.format("In transaction after first insert: count=%d", libraryDao.countOf()));
                //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
                assertEquals(1, libraryDao.countOf(), "Row count should be 1 after first insert");

                LibraryEntity library2 = new LibraryEntity();
                library2.setName(null); // this should violate the not-null constraint
                libraryDao.create(library2);

                fail("We should never get here");
                return null;
            });
            fail("We should never get here either");
        } catch (SQLException e) {
            //e.printStackTrace();
            assertEquals("23502", e.getSQLState(), "We should have failed on a not-null constraint");
            assertEquals(0, libraryDao.countOf(), "Row count should be zero after aborted transaction");
            //System.out.println("In catch");
            //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
        }
        assertEquals(0, libraryDao.countOf(), "Row count should be 0 after try/catch block");
        //System.out.println("After try/catch");
        //libraryDao.forEach(lib -> System.out.println(String.format("Library %d: %s", lib.getLibraryId(), lib.getName())));
    }

    @AfterAll
    public static void tearDown() throws SQLException, IOException {
        TableUtils.dropTable(connectionSource, LibraryEntity.class, false);
        connectionSource.close();
    }
}