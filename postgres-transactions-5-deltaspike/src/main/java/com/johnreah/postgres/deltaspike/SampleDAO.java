package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class SampleDAO {

    @Inject
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Transactional
    public long createSample(int intValue, String stringValue) {
        ReportTransactionStatus("SampleDAO#createSample", entityManager);
        SampleEntity sampleEntity = new SampleEntity(intValue, stringValue);
        entityManager.persist(sampleEntity);
        long id = sampleEntity.getId().longValue();
        System.out.println("SampleDAO#createSample: created sample with id: " + id);
        return id;
    }

    public String getStringFromId(long id) {
        ReportTransactionStatus("SampleDAO#getStringFromId", entityManager);
        return entityManager.createQuery("select e.stringValue from SampleEntity e where e.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Transactional
    public long countAll() {
        ReportTransactionStatus("SampleDAO#countAll", entityManager);
        return entityManager
                .createQuery("select count(e) from SampleEntity e", Long.class)
                .getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        ReportTransactionStatus("SampleDAO#deleteAll", entityManager);
        entityManager
                .createQuery("delete from SampleEntity ")
                .executeUpdate();
    }

    private void ReportTransactionStatus(String methodName, EntityManager entityManager) {
        System.out.println(String.format("%s | EM: %s | Transaction active: %s | EM joint to txn: %s",
                methodName,
                entityManager.toString().substring(entityManager.toString().indexOf("@")),
                entityManager.getTransaction().isActive() ? "true" : "false",
                entityManager.isJoinedToTransaction() ? "true" : "false"));
    }

}
