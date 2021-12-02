package com.johnreah.postgres.cdi;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProvider {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public EntityManagerProvider() {
        entityManagerFactory = Persistence.createEntityManagerFactory("postgres_transactions_cdi");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Produces
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

//    @Produces
//    public static EntityManager getEntityManager() {
//        return Persistence.createEntityManagerFactory("postgres_transactions_cdi").createEntityManager();
//    }

    public static Object getEntityManager(InjectionPoint injectionPoint) {
        if (entityManager == null) {
            entityManager = Persistence.createEntityManagerFactory("postgres_transactions_cdi").createEntityManager();
        }
        return entityManager;
    }
}
