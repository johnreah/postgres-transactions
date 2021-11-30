package com.johnreah.cdi_jta_helper.junit5;

import com.johnreah.cdi_jta_helper.cdi.CDITransactionServices;
import com.johnreah.cdi_jta_helper.cdi.JTEMInterceptor;
import com.johnreah.cdi_jta_helper.jndihelper.SeMemoryContext;
import com.johnreah.cdi_jta_helper.jndihelper.SeMemoryContextFactory;
import com.johnreah.cdi_jta_helper.persistence.H2DataSourceWrapper;
import com.johnreah.cdi_jta_helper.persistence.H2EntityManagerFactoryProducer;
import com.johnreah.cdi_jta_helper.persistence.H2TransactionalDataSourceDelegate;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.auto.WeldJunit5AutoExtension;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * extended autoextension with the narayana transaction manager.
 * all needed bean classes are added and the TransactionExtension
 * / CDITransactionService are registered
 * @author Michael Krauter
 *
 */
public class WeldJunit5NarayanaAutoExtension extends WeldJunit5AutoExtension{

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
