package com.johnreah.cdi_jta_helper.junit5;

import com.johnreah.cdi_jta_helper.cdi.CDITransactionServices;
import com.johnreah.cdi_jta_helper.cdi.JTEMInterceptor;
import com.johnreah.cdi_jta_helper.jndihelper.SeMemoryContext;
import com.johnreah.cdi_jta_helper.jndihelper.SeMemoryContextFactory;
import com.johnreah.cdi_jta_helper.persistence.H2DataSourceWrapper;
import com.johnreah.cdi_jta_helper.persistence.H2EntityManagerFactoryProducer;
import com.johnreah.cdi_jta_helper.persistence.H2TransactionalDataSourceDelegate;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 * identical setup to the WeldJunit5NarayanaAutoExtension.class
 * @author Michael Krauter
 *
 */
public class WeldJunit5NarayanaExtension extends WeldJunit5Extension {

		@Override
		protected void weldInit(Object testInstance,ExtensionContext context,Weld weld,WeldInitiator.Builder weldInitiatorBuilder) {
			SeMemoryContextFactory.initSystemProperties();
			weld.addBeanClasses(
    		SeMemoryContext.class, H2EntityManagerFactoryProducer.class,
    		H2DataSourceWrapper.class, H2TransactionalDataSourceDelegate.class,
    		JTEMInterceptor.class
    		);
			
			weld.addExtension(new com.arjuna.ats.jta.cdi.TransactionExtension());
			weld.addServices(new CDITransactionServices());
			weld.addInterceptor(JTEMInterceptor.class);
		    super.weldInit(testInstance, context, weld, weldInitiatorBuilder);
		}

}
