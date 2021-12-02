package com.johnreah.cdi_jta_helper.persistence;

import com.arjuna.ats.jta.common.jtaPropertyManager;
import org.eclipse.persistence.platform.server.JMXServerPlatformBase;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.transaction.JTATransactionController;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;

/**
 * needed by eclipselink to get the external transaction controller registered.
 * @author Michael Krauter
 *
 */
public class SeServerPlatform extends JMXServerPlatformBase{

	public SeServerPlatform(DatabaseSession newDatabaseSession) {
		super(newDatabaseSession);
	}

	@Override
	public Class getExternalTransactionControllerClass() {
		return TestTransactionController.class;
	}
	
	public int getJNDIConnectorLookupType() {
		return JNDIConnector.COMPOSITE_NAME_LOOKUP;
	}
	
	public static class TestTransactionController extends JTATransactionController{
		protected TransactionManager acquireTransactionManager() throws Exception{
			return InitialContext.doLookup(jtaPropertyManager.getJTAEnvironmentBean().getTransactionManagerJNDIContext());
		}  
	}
	
}
