package com.johnreah.postgres.deltaspike.services;

import com.johnreah.postgres.deltaspike.repositories.SampleDAO;
import com.johnreah.postgres.deltaspike.services.SampleService;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SampleServiceImpl implements SampleService {

    @Inject
    SampleDAO sampleDAO;

    @Override
    @Transactional
    public long createSample(int intValue, String stringValue) {
        return sampleDAO.createSample(intValue, stringValue);
    }

    @Override
    @Transactional
    public List<Long> createSamplesTransactional(Map<Integer, String> samples) {
        ReportTransactionStatus("SampleServiceImpl#createSamplesTransactional", sampleDAO.getEntityManager());
        List<Long> sampleIds = new ArrayList<>();
        for (int i: samples.keySet()) {
            long id = sampleDAO.createSample(i, samples.get(i));
            sampleIds.add(id);
        }
        return sampleIds;
    }

    @Override
    public List<Long> createSamplesNonTransactional(Map<Integer, String> samples) {
        ReportTransactionStatus("SampleServiceImpl#createSamplesNonTransactional", sampleDAO.getEntityManager());
        List<Long> sampleIds = new ArrayList<>();
        for (int i: samples.keySet()) {
            long id = sampleDAO.createSample(i, samples.get(i));
            sampleIds.add(id);
            System.out.println("SampleCount: " + sampleDAO.countAll());
        }
        return sampleIds;
    }

    @Override
    @Transactional
    public String getStringFromId(long id) {
        return sampleDAO.getStringFromId(id);
    }

    @Override
    @Transactional
    public long countSamples() {
        return sampleDAO.countAll();
    }

    @Override
    @Transactional
    public void deleteAllSamples() {
        ReportTransactionStatus("SampleServiceImpl#deleteAllSamples", sampleDAO.getEntityManager());
        sampleDAO.deleteAll();
    }

    private void ReportTransactionStatus(String methodName, EntityManager entityManager) {
        System.out.println(String.format("%s | EM: %s | Transaction active: %s | EM joint to txn: %s",
                methodName,
                entityManager.toString().substring(entityManager.toString().indexOf("@")),
                entityManager.getTransaction().isActive() ? "true" : "false",
                entityManager.isJoinedToTransaction() ? "true" : "false"));
    }

}
