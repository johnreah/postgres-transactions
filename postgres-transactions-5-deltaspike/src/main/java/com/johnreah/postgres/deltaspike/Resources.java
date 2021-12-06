package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class Resources {

    @Inject
    @PersistenceUnitName("postgres_transactions_deltaspike")
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @RequestScoped
    public EntityManager create() {
        return this.entityManagerFactory.createEntityManager();
    }

    public void dispose(@Disposes @Default EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}
