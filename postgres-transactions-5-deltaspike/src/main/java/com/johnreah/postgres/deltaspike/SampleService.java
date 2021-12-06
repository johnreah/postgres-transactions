package com.johnreah.postgres.deltaspike;

import java.util.List;
import java.util.Map;

public interface SampleService {

        long createSample(int intValue, String stringValue);

        List<Long> createSamplesTransactional(Map<Integer, String> sampleEntities);

        List<Long> createSamplesNonTransactional(Map<Integer, String> sampleEntities);

        String getStringFromId(long id);

        long countSamples();

        void deleteAllSamples();

    }
