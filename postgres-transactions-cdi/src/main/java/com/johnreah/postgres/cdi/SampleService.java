package com.johnreah.postgres.cdi;

public interface SampleService {

        int returnFive();

        long createSampleEntity(int intValue, String stringValue);

        String getStringValueFor(long id);


    }
