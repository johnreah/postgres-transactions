package com.johnreah.postgres.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SampleServiceImpl implements SampleService {

    @Inject
    SampleDAO sampleDAO;

    @Override
    public int returnFive() {
        return sampleDAO.returnFive();
    }

    @Override
    public long createSample(int intValue, String stringValue) {
        return sampleDAO.createSample(intValue, stringValue);
    }

    @Override
    @Transactional
    public List<Long> createSamplesTransactional(Map<Integer, String> samples) {
        List<Long> sampleIds = new ArrayList<>();
        for (int i: samples.keySet()) {
            long id = sampleDAO.createSample(i, samples.get(i));
            sampleIds.add(id);
        }
        return sampleIds;
    }

    @Override
    @Transactional(Transactional.TxType.NEVER)
    public List<Long> createSamplesNonTransactional(Map<Integer, String> samples) {
        List<Long> sampleIds = new ArrayList<>();
        for (int i: samples.keySet()) {
            long id = sampleDAO.createSample(i, samples.get(i));
            sampleIds.add(id);
            System.out.println("SampleCount: " + sampleDAO.countAll());
        }
        return sampleIds;
    }

    @Override
    public String getStringFromId(long id) {
        return sampleDAO.getStringFromId(id);
    }

    @Override
    public long countSamples() {
        return sampleDAO.countAll();
    }

    @Override
    public void deleteAllSamples() {
        sampleDAO.deleteAll();
    }
}
