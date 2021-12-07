package com.johnreah.postgres.cdi;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.SynchronizationType;
import java.io.Serializable;

@ApplicationScoped
public class EntityManagerProducer implements Serializable {

    private static final long serialVersionUID = -3357459309592123852L;

    @Inject
    private EntityManagerFactory emf;

    @Produces
    @RequestScoped
    public EntityManager newEntityManager() {
        return emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

}
