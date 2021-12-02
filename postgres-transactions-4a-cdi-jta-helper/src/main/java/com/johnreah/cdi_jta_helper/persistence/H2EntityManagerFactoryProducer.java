package com.johnreah.cdi_jta_helper.persistence;

import com.arjuna.ats.jta.utils.JNDIManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.CompositeName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * responsible for entitymanagerfactory setup. also binds the
 * both datasources to simplejndi. the jndi resources (values are strings)
 * are: 
 * "java:h2SetupScript": the dml-script to setup the database 
 * "java:persistentCtxName": the name of the persistent context
 * "java:jndiJTADsName": datasource jndi name of the JTA DataSource
 * "java:jndiNONJTADsName": datasource jndi name of the non JTA Datasource (read)
 * @author mk
 *
 */

@ApplicationScoped
public class H2EntityManagerFactoryProducer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6103386914428711458L;

	public static final String jndi_SetupDbScriptName = "java:h2SetupScript";
	public static final String jndi_PersistentContextName = "java:persistentCtxName";
	public static final String jndi_JTADataSourceName = "java:jndiJTADsName";
	public static final String jndi_NON_JTADataSourceName = "java:jndiNONJTADsName";
	
	
    @Resource(lookup = H2EntityManagerFactoryProducer.jndi_NON_JTADataSourceName)
	private String datasourceName;
    
    @Resource(lookup = H2EntityManagerFactoryProducer.jndi_JTADataSourceName)
    private String jtaDsDelegate;
    
	@Inject
	private BeanManager beanManager;

	@Resource(lookup = H2EntityManagerFactoryProducer.jndi_SetupDbScriptName)
	private String scriptpathAndName;
	
	@Resource(lookup = H2EntityManagerFactoryProducer.jndi_PersistentContextName)
	private String persistentCtxName;
	
	@Inject
	H2DataSourceWrapper datasource;
	
	@Inject
	H2TransactionalDataSourceDelegate tdriver;

	
	@PostConstruct
	public void init() throws NamingException{
		InitialContext ctx = new InitialContext();
		ctx.bind(new CompositeName(this.jtaDsDelegate),this.tdriver);
		ctx.bind(new CompositeName(this.datasourceName), this.datasource);
		// bypass transactional driver for reading and sequences
		JNDIManager.bindJTAImplementation();
	}
	
	
	@Produces
	@ApplicationScoped
	public EntityManagerFactory produceEntityManagerFactory() {
	    Map<String, Object> m = new HashMap<>();
	    m.put("javax.persistence.bean.manager", beanManager);
	    m.put("external-transaction-controller","true");
	    m.put("eclipselink.target-database","org.eclipse.persistence.platform.database.H2Platform");
		m.put("eclipselink.target-server","com.johnreah.cdi_jta_helper.persistence.SeServerPlatform");
		m.put("javax.persistence.schema-generation.database.action","drop-and-create");
		m.put("javax.persistence.sql-load-script-source",this.scriptpathAndName);
		m.put("eclipselink.deploy-on-startup","true");
		m.put("eclipselink.cache.shared.default","true");
		m.put("eclipselink.validation-only","false");
        // override settings for testing	
	    return Persistence.createEntityManagerFactory(this.persistentCtxName, m);
	}
		

	public void close(@Disposes EntityManagerFactory entityManagerFactory) {
        entityManagerFactory.close();
    }
    
	
}
