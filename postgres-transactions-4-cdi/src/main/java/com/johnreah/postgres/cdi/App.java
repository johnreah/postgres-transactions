package com.johnreah.postgres.cdi;

public class App {

    public static void main(String[] args) {
        System.out.println("""
            The unit tests are the important part of this example. The JUnit test extension enables unit
            tests using a real transaction manager (Narayana, as used in WildFly) and real JPA, but uses an
            in-memory database and no heavyweight EE container (like WildFly) or EE test framework (like
            Arquillian). This means the tests run quickly but exhibit real transactional behaviour, which
            is good for unit testing the service and the persistence layers of an application.""");
    }

}
