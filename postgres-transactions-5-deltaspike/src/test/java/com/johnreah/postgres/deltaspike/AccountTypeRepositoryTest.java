package com.johnreah.postgres.deltaspike;

import com.johnreah.postgres.deltaspike.entities.AccountTypeEntity;
import com.johnreah.postgres.deltaspike.repositories.AccountTypeRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CdiTestRunner.class)
public class AccountTypeRepositoryTest {

    @Inject
    AccountTypeRepository accountTypeRepository;

    @Test
    public void needARepository() {
        assertNotNull("We need a non-null repository", accountTypeRepository);
    }

    @Test
    public void whenCreateAccountType_thenPersistsOk() {
        AccountTypeEntity accountTypeEntity = new AccountTypeEntity("description", "reference", null);
        accountTypeRepository.saveAndFlush(accountTypeEntity);
        assertEquals("Account should have saved ok", Long.valueOf(1), accountTypeRepository.count());
    }

}