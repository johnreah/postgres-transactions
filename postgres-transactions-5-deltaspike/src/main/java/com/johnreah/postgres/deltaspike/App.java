package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;

/**
 * Run with -javaagent:<some-path>/eclipselink-2.7.9.jar to enable dynamic weaving
 */
public class App {

    public static void main(String[] args) {

        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        ContextControl contextControl = cdiContainer.getContextControl();
        contextControl.startContext(ApplicationScoped.class);
        contextControl.startContext(RequestScoped.class);

        Runnable runnable = BeanProvider.getContextualReference(Runnable.class, false);

        runnable.run();

        cdiContainer.shutdown();
    }

}
