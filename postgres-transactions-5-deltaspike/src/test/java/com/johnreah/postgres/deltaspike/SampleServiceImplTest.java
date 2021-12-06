package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@RunWith(CdiTestRunner.class)
public class SampleServiceImplTest {

    @Inject
    SampleService sampleService;

    @Test
    public void checkInjection() {
        assertNotNull("Need a sample service", sampleService);
    }

}