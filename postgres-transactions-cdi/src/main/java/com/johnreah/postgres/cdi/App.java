package com.johnreah.postgres.cdi;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.lang.reflect.Field;

public class App {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException {

        // Application-managed entity management
        EntityManager entityManager = Persistence.createEntityManagerFactory("postgres_transactions_cdi").createEntityManager();

        // Inject entity manager
        SampleService sampleService;
        sampleService = SampleServiceImpl.class.newInstance();
        Field field = sampleService.getClass().getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(sampleService, entityManager);

        // Do something with data
        entityManager.getTransaction().begin();
        Long id = sampleService.createSampleEntity(111, "one-one-one");
        String s = sampleService.getStringValueFor(id);
        entityManager.getTransaction().commit();

        System.out.println("Got: " + s);
    }

}
