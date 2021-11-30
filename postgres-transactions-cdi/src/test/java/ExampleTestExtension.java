import com.johnreah.cdi_jta_helper.junit5.WeldJunit5NarayanaExtension;
import com.johnreah.cdi_jta_helper.persistence.H2DataSourceWrapper;
import com.johnreah.cdi_jta_helper.persistence.H2EntityManagerFactoryProducer;
import com.johnreah.postgres.cdi.EntityManagerProducer;
import com.johnreah.postgres.cdi.SampleDAO;
import com.johnreah.postgres.cdi.SampleService;
import com.johnreah.postgres.cdi.SampleServiceImpl;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.WeldInitiator;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * extension for setting up the container
 * @author Michael Krauter
 *
 */
public class ExampleTestExtension extends WeldJunit5NarayanaExtension {

	@Override
	protected void weldInit(Object testInstance, ExtensionContext context, Weld weld, WeldInitiator.Builder weldInitiatorBuilder) {
		// we cant add services while doing @WeldSetup so init is placed here
        weld.addBeanClasses(
        		SampleServiceImpl.class,
				SampleDAO.class,
				EntityManagerProducer.class
        		);
		weldInitiatorBuilder.bindResource(H2DataSourceWrapper.jndi_H2Url,"jdbc:h2:mem:test_db;MODE=Oracle;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
        // following values according to persistence.xml
        weldInitiatorBuilder.activate(RequestScoped.class,SessionScoped.class);
		weldInitiatorBuilder.bindResource(H2EntityManagerFactoryProducer.jndi_PersistentContextName,"postgres_transactions_cdi");
//		weldInitiatorBuilder.bindResource(H2EntityManagerFactoryProducer.jndi_SetupDbScriptName,"dbtest/dbinit.sql");
		weldInitiatorBuilder.bindResource(H2EntityManagerFactoryProducer.jndi_NON_JTADataSourceName,"java:read_ds");
		weldInitiatorBuilder.bindResource(H2EntityManagerFactoryProducer.jndi_JTADataSourceName,"java:example_ds");
		// these values must correspond with your persistence.xml


//		weldInitiatorBuilder.setPersistenceContextFactory(EntityManagerProviderJR::getEntityManagerStatic);
		weldInitiatorBuilder.setPersistenceContextFactory((InjectionPoint injectionPoint) ->{
			return new EntityManagerProducer().newEntityManager();
		});

		super.weldInit(testInstance, context, weld, weldInitiatorBuilder);
	}

}
