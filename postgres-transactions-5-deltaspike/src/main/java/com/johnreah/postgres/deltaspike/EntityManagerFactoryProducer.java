package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
public class EntityManagerFactoryProducer {

    @Inject
    @PersistenceUnitName("postgres_transactions_deltaspike")
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @ApplicationScoped
    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

}
