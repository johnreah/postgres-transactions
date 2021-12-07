package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(CdiTestRunner.class)
public class AccountRepositoryTest {

    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountTypeRepository accountTypeRepository;

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
}