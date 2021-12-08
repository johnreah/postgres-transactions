package com.johnreah.postgres.cdi;

public class App {

    public static void main(String[] args) {
        System.out.println(
            "The unit tests are the important part of this example. The JUnit test extension enables unit\n" +
            "tests using a real transaction manager (Narayana, as used in WildFly) and real JPA, but uses an\n" +
            "in-memory database and no heavyweight EE container (like WildFly) or EE test framework (like\n" +
            "Arquillian). This means the tests run quickly but exhibit real transactional behaviour, which\n" +
            "is good for unit testing the service and the persistence layers of an application.");
    }

}
