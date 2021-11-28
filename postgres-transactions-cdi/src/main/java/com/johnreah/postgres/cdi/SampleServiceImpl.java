package com.johnreah.postgres.cdi;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class SampleServiceImpl implements SampleService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public int returnFive() {
        return 5;
    }

    @Override
    public long createSampleEntity(int intValue, String stringValue) {
        System.out.println("Transaction active: " + entityManager.getTransaction().isActive());
        SampleEntity sampleEntity = new SampleEntity(intValue, stringValue);
        entityManager.persist(sampleEntity);
        return sampleEntity.getId().longValue();
    }

    @Override
    public String getStringValueFor(long id) {
        return entityManager.createQuery("select e.stringValue from SampleEntity e where e.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
