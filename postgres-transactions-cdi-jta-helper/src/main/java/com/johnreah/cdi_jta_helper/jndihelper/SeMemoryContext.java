package com.johnreah.cdi_jta_helper.jndihelper;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple;
import org.osjava.sj.memory.MemoryContext;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;


/**
 * helpercontext because of 
 * simplejndi can't deal with javax.naming.reference (used by Narayana)
 * so we need a little bit magic here..
 * @author Michael Krauter
 *
 */
@ApplicationScoped
public class SeMemoryContext extends MemoryContext {

	TransactionManager transactionManager;
	TransactionSynchronizationRegistry syncReg;
		
	@PostConstruct
	private void init() throws Exception{	
		this.transactionManager =  new TransactionManagerImple();
	    this.syncReg = new TransactionSynchronizationRegistryImple();		
		
	}
	
	public Object lookup(Name name) throws NamingException{
		// determine which object needs lookup
		if (name.toString().toLowerCase().contains("transactionmanager")) {
			return this.transactionManager;
		}else if(name.toString().toLowerCase().contains("synchronization")) {
			return this.syncReg;
		}else if(name.toString().toLowerCase().contains("usertransaction")) {
		    return super.lookup(name);				
		}else {
			return super.lookup(name);
		}
	}
	
	@Override
	public void close() throws NamingException{
		// never close the in-memory context
	}
	
}
