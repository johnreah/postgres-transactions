package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
@Alternative
public class TestEntityManagerFactoryProducer {

    @Inject
    @PersistenceUnitName("test")
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @ApplicationScoped
    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

}
