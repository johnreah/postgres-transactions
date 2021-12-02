package com.johnreah.cdi_jta_helper.cdi;

import org.jboss.logging.Logger;
import org.jboss.weld.transaction.spi.TransactionServices;

import javax.transaction.*;

/**
 * src from: https://github.com/jbosstm/quickstart/blob/master/jta-1_2-standalone/src/test/java/org/jboss/narayana/quickstarts/jta/cdi/CDITransactionServices.java
 * 
 * SPI extension point of the Weld for integrate with transaction manager.
 * If the interface is implemented by the deployment the Weld stops to show
 * info message:
 * <p>
 * <code>
 *  WELD-000101: Transactional services not available. Injection of @Inject UserTransaction not available.
 *    Transactional observers will be invoked synchronously.
 * </code>
 * </p>
 */
public class CDITransactionServices implements TransactionServices {
    private static final Logger LOG = Logger.getLogger(CDITransactionServices.class);

    @Override
    public void registerSynchronization(Synchronization synchronizedObserver) {
        try {
            com.arjuna.ats.jta.TransactionManager.transactionManager()
                .getTransaction().registerSynchronization(synchronizedObserver);
        } catch (SystemException | IllegalStateException | RollbackException e) {
            throw new IllegalStateException("Cannot register synchronization observer " + synchronizedObserver
                    + " to the available transaction", e);
        }
    }

    @Override
    public boolean isTransactionActive() {
        try {
            int status = com.arjuna.ats.jta.TransactionManager.transactionManager().getStatus();
            switch(status) {
                case Status.STATUS_ACTIVE:
                case Status.STATUS_COMMITTING:
                case Status.STATUS_MARKED_ROLLBACK:
                case Status.STATUS_PREPARED:
                case Status.STATUS_PREPARING:
                case Status.STATUS_ROLLING_BACK:
                    return true;
                default:
                    return false;
            }
        } catch (SystemException se) {
            LOG.error("Cannot obtain the status of the transaction", se);
            return false;
        }
    }

    @Override
    public UserTransaction getUserTransaction() {
        return com.arjuna.ats.jta.UserTransaction.userTransaction();
    }

    @Override
    public void cleanup() {
        // nothing to clean
    }		
		
}
