package com.johnreah.postgres.deltaspike;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.util.HashMap;
import java.util.Map;

public class Runnable {

    @Inject
    SampleService sampleService;

    public void run() {

        try {
            sampleService.deleteAllSamples();
            Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
                put(1, "one");
                put(2, "two");
                put(3, "two");
            }};
            sampleService.createSamplesNonTransactional(newSampleValues);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        System.out.println("Count: " + sampleService.countSamples());

        System.out.println("String for id 2: " + sampleService.getStringFromId(2));
    }
}
