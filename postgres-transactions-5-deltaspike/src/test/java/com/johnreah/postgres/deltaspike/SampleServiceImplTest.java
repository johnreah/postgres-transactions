package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CdiTestRunner.class)
public class SampleServiceImplTest {

    @Inject
    SampleService sampleService;

    @Test
    public void checkInjection() {
        assertNotNull("Need a sample service", sampleService);
    }

    @Test
    public void givenNonTransactionalMethod_whenFailsHalfWayThrough_thenDebrisLeftBehind() {
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
        assertEquals("", 2, sampleService.countSamples());

    }

    @Test
    public void givenTransactionalMethod_whenFailsHalfWayThrough_thenRollback() {
        try {
            sampleService.deleteAllSamples();
            Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
                put(1, "one");
                put(2, "two");
                put(3, "two");
            }};
            sampleService.createSamplesTransactional(newSampleValues);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        System.out.println("Count: " + sampleService.countSamples());
        assertEquals("", 0, sampleService.countSamples());

    }
}
