package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(CdiTestRunner.class)
public class BankingServiceTest {

    @Inject
    BankingService bankingService;

    @Before
    public void before() {
        bankingService.deleteEverything();
    }

    @Test
    public void checkInjection() {
        assertNotNull("We need a BankingService", bankingService);
    }

    @Test
    public void givenNoTransaction_whenMultipleGoodCreates_thenAllCreatesPersistOk() {
        long numAccountTypesBefore = bankingService.countAccountTypes();
        long numAccountsBefore = bankingService.countAccounts();

        bankingService.createContrivedAccountTypeWithThreeAccountsNonTransactional("ref1", "ref2", "ref3");

        assertEquals("Should have created 3 accounts", 3, bankingService.countAccounts() - numAccountsBefore);
        assertEquals("Should have created 1 account type", 1, bankingService.countAccountTypes() - numAccountTypesBefore);
    }

    @Test
    public void givenNoTransaction_whenMultipleCreatesIncludeFailure_thenDebrisLeftBehind() {
        long numAccountTypesBefore = bankingService.countAccountTypes();
        long numAccountsBefore = bankingService.countAccounts();

        bankingService.createContrivedAccountTypeWithThreeAccountsNonTransactional("ref1", "ref2", "ref2");

        assertEquals("Should have created 2 accounts", 2, bankingService.countAccounts() - numAccountsBefore);
        assertEquals("Should have created 1 account type", 1, bankingService.countAccountTypes() - numAccountTypesBefore);
    }

    @Test
    public void givenTransaction_whenMultipleGoodCreates_thenAllCreatesPersistOk() {
        long numAccountTypesBefore = bankingService.countAccountTypes();
        long numAccountsBefore = bankingService.countAccounts();

        bankingService.createContrivedAccountTypeWithThreeAccountsTransactional("ref1", "ref2", "ref3");

        assertEquals("Should have created 3 accounts", 3, bankingService.countAccounts() - numAccountsBefore);
        assertEquals("Should have created 1 account type", 1, bankingService.countAccountTypes() - numAccountTypesBefore);
    }

    @Test
    public void givenTransaction_whenMultipleCreatesIncludeFailure_thenAllDebrisIsCleanedUp() {
        long numAccountTypesBefore = bankingService.countAccountTypes();
        long numAccountsBefore = bankingService.countAccounts();

        bankingService.createContrivedAccountTypeWithThreeAccountsTransactional("ref1", "ref2", "ref2");

        assertEquals("Should have created 0 accounts", 0, bankingService.countAccounts() - numAccountsBefore);
        assertEquals("Should have created 0 account types", 0, bankingService.countAccountTypes() - numAccountTypesBefore);
    }

}