package com.johnreah.postgres.cdi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class SampleDAO {

//    @PersistenceContext
    @Inject
    EntityManager entityManager;

    public int returnFive() {
        return 5;
    }

    public long createSample(int intValue, String stringValue) {
        System.out.println("isJoinedToTransaction: " + entityManager.isJoinedToTransaction());
        SampleEntity sampleEntity = new SampleEntity(intValue, stringValue);
        entityManager.persist(sampleEntity);
        return sampleEntity.getId().longValue();
    }

    public String getStringFromId(long id) {
        return entityManager.createQuery("select e.stringValue from SampleEntity e where e.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public long countAll() {
        return entityManager
                .createQuery("select count(e) from SampleEntity e", Long.class)
                .getSingleResult();
    }

    public void deleteAll() {
        entityManager
                .createQuery("delete from SampleEntity ")
                .executeUpdate();
    }

}
