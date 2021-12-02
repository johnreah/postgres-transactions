package com.johnreah.postgres.cdi;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.lang.reflect.Field;

public class App {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        new App().run();
    }

    private void run() throws InstantiationException, IllegalAccessException, NoSuchFieldException {

        EntityManager entityManager = Persistence
                .createEntityManagerFactory("postgres_transactions_cdi")
                .createEntityManager();

        SampleDAO sampleDAO = SampleDAO.class.newInstance();
        Field field = sampleDAO.getClass().getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(sampleDAO, entityManager);

        SampleService sampleService = SampleServiceImpl.class.newInstance();
        field = sampleService.getClass().getDeclaredField("sampleDAO");
        field.setAccessible(true);
        field.set(sampleService, sampleDAO);

        sampleService.deleteAllSamples();
        sampleService.createSample(111, "one");
        System.out.println("Count: " + sampleService.countSamples());
    }

}
