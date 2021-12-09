package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import static org.junit.Assert.*;

@RunWith(CdiTestRunner.class)
public class AccountRepositoryTest {

    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountTypeRepository accountTypeRepository;

    @Before
    public void before() {
        accountRepository.findAll().stream().forEach(accountRepository::removeAndFlush);
        accountTypeRepository.findAll().stream().forEach(accountTypeRepository::removeAndFlush);
    }

    @Test
    public void checkInjection() {
        assertNotNull("We need an AccountRepository", accountRepository);
        assertNotNull("We need an AccountTypeRepository", accountTypeRepository);
    }

    @Test
    public void whenAccountCreated_thenAccountPersists() {
        List<AccountTypeEntity> accountTypeEntities = accountTypeRepository.findByDescription("current account");
        AccountTypeEntity currentAccountType;
        if (accountTypeEntities.isEmpty()) {
            AccountTypeEntity accountTypeEntity = new AccountTypeEntity("current account", "ref", null);
            currentAccountType = accountTypeRepository.saveAndFlush(accountTypeEntity);
        } else {
            currentAccountType = accountTypeEntities.get(0);
        }
        assertNotNull("We need a current account type", currentAccountType);
        assertNotEquals("Current account type needs an ID assigned", Long.valueOf(0), currentAccountType.getId());

        AccountEntity accountEntity = new AccountEntity(currentAccountType, "description", 0.0, Date.from(Instant.now()), "ref");
        accountRepository.saveAndFlush(accountEntity);
        assertNotEquals("new account needs an ID assigned", Long.valueOf(0), accountEntity.getId());
    }

    @Test
    public void givenAccountOfType_whenQueryByType_thenFound() {
        AccountTypeEntity accountType1 = accountTypeRepository.saveAndFlush(new AccountTypeEntity("accountType1", "accountType1", null));
        AccountTypeEntity accountType2 = accountTypeRepository.saveAndFlush(new AccountTypeEntity("accountType2", "accountType2", null));
        AccountEntity account1_1 = accountRepository.saveAndFlush(new AccountEntity(accountType1, "account1_1", 0.0, Date.from(Instant.now()), "account1_1"));
        AccountEntity account1_2 = accountRepository.saveAndFlush(new AccountEntity(accountType1, "account1_2", 0.0, Date.from(Instant.now()), "account1_2"));
        AccountEntity account2_1 = accountRepository.saveAndFlush(new AccountEntity(accountType2, "account2_1", 0.0, Date.from(Instant.now()), "account2_1"));
        AccountEntity account2_2 = accountRepository.saveAndFlush(new AccountEntity(accountType2, "account2_2", 0.0, Date.from(Instant.now()), "account2_2"));
        assertEquals("There should be 2 accounts of type 1", 2, accountRepository.findByAccountType(accountType1).size());
    }

    @Test
    public void givenAccountTypeAndAccount_whenAccountFetched_thenAccountTypePopulated() {
        AccountTypeEntity accountType = accountTypeRepository.saveAndFlush(new AccountTypeEntity("accountType", "accountType", null));
        AccountEntity account = accountRepository.saveAndFlush(new AccountEntity(accountType, "account", 0.0, Date.from(Instant.now()), "account"));
        AccountEntity accountFetched = accountRepository.findBy(account.getId());
        assertNotNull("Account should have a type", accountFetched.getAccountType());
        assertEquals("Account type should be populated", "accountType", accountFetched.getAccountType().getDescription());
    }

    @Test
    public void givenAccountTypeAndAccounts_whenAccountTypeFetched_thenAccountsNotPopulated() {
        AccountTypeEntity accountType = accountTypeRepository.save(new AccountTypeEntity("accountType", "accountType", null));
        AccountEntity account1 = accountRepository.save(new AccountEntity(accountType, "account1", 0.0, Date.from(Instant.now()), "account1"));
        AccountEntity account2 = accountRepository.save(new AccountEntity(accountType, "account2", 0.0, Date.from(Instant.now()), "account2"));
        AccountEntity account3 = accountRepository.save(new AccountEntity(accountType, "account3", 0.0, Date.from(Instant.now()), "account3"));
        assertNotEquals("Saved AccountType ahould have an id", Long.valueOf(0), accountType.getId());
        assertNotEquals("Saved Account ahould have an id", Long.valueOf(0), account1.getId());
        assertEquals("AccountType lazy-load of child accounts can't work yet because we haven't added them", 0, accountType.getAccounts().size());

        accountTypeRepository.saveAndFlushAndRefresh(accountType);
        accountType.getAccounts().stream().forEach((AccountEntity ae) -> {System.out.println("AE: " + ae.getReference());});
        assertEquals("AccountType child accounts should be populated after refresh", 3, accountType.getAccounts().size());
    }

    @Test
    public void givenAccountTypeAndAccounts_whenAccountTypeFetchedWithAccounts_thenAccountsPopulated() {
        AccountTypeEntity accountType = accountTypeRepository.saveAndFlush(new AccountTypeEntity("accountType", "accountType", null));
        AccountEntity account1 = accountRepository.saveAndFlush(new AccountEntity(accountType, "account1", 0.0, Date.from(Instant.now()), "account1"));
        AccountEntity account2 = accountRepository.saveAndFlush(new AccountEntity(accountType, "account2", 0.0, Date.from(Instant.now()), "account2"));
        AccountEntity account3 = accountRepository.saveAndFlush(new AccountEntity(accountType, "account3", 0.0, Date.from(Instant.now()), "account3"));

        // A list of Vectors each containing an AccountType and an Account
        List<Object> accountTypeVectorsFetched = accountTypeRepository.findByIdWithAccounts(accountType.getId());
        assertEquals("Lookup by account type should have returned 3 accounts", 3, accountTypeVectorsFetched.size());
    }

}
