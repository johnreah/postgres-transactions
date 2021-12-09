package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
//@Alternative
//@Priority(Interceptor.Priority.APPLICATION - 1)
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
