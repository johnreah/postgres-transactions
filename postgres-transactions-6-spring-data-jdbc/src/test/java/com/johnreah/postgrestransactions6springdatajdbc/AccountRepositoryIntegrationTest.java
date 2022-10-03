package com.johnreah.postgrestransactions6springdatajdbc;

import com.johnreah.postgrestransactions6springdatajdbc.entities.Account;
import com.johnreah.postgrestransactions6springdatajdbc.entities.AccountHistory;
import com.johnreah.postgrestransactions6springdatajdbc.entities.AccountType;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.AccountRepository;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.AccountTypeRepository;
import com.johnreah.postgrestransactions6springdatajdbc.support.AbstractIntegrationTest;
import com.johnreah.postgrestransactions6springdatajdbc.support.DatabaseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    private Logger log = LoggerFactory.getLogger(AccountRepositoryIntegrationTest.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Autowired
    DatabaseUtils databaseUtils;

    @BeforeEach
    public void beforeEach() {
        databaseUtils.deleteEverything();
    }

    @Test
    public void testPersistence() {
        AccountType accountType = AccountType.builder()
                .description("Test account type")
                .reference("TEST_REF")
                .build();
        accountType = accountTypeRepository.save(accountType);
        assertTrue(accountType != null && accountType.getId() != 0, "AccountType should have saved");

        Account account = Account.builder()
                .description("Test account")
                .accountTypeId(AggregateReference.to(accountType.getId()))
                .build();

        long countBeforeSave = accountRepository.count();
        long id = accountRepository.save(account).getId();
        assertTrue(id != 0, "ID should be non-zero after save");
        long countAfterSave = accountRepository.count();
        assertTrue(countAfterSave == countBeforeSave + 1, "Count should have incremented by 1");

        AccountHistory accountHistory1 = AccountHistory.builder()
                .description("Test account history one")
                .build();
        AccountHistory accountHistory2 = AccountHistory.builder()
                .description("Test account history two")
                .build();
        AccountHistory accountHistory3 = AccountHistory.builder()
                .description("Test account history three")
                .build();
        account.getAccountHistories().add(accountHistory1);
        account.getAccountHistories().add(accountHistory2);
        account.getAccountHistories().add(accountHistory3);
        accountRepository.save(account);
        long countAfterUpdate = accountRepository.count();
        assertTrue(countAfterUpdate == countAfterSave, "Update should keep count the same");

        Optional<Account> accountReloaded = accountRepository.findById(id);
        assertTrue(accountReloaded.isPresent() && accountReloaded.get().getAccountHistories().size() == 3, "All history records should have persisted");
    }

}
