package com.johnreah.postgrestransactions6springdatajdbc;

import com.johnreah.postgrestransactions6springdatajdbc.entities.Account;
import com.johnreah.postgrestransactions6springdatajdbc.entities.AccountHistory;
import com.johnreah.postgrestransactions6springdatajdbc.entities.AccountType;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.AccountRepository;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.AccountTypeRepository;
import com.johnreah.postgrestransactions6springdatajdbc.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    private Logger log = LoggerFactory.getLogger(AccountRepositoryIntegrationTest.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Test
    public void testPersistence() {
        AccountType accountType = accountTypeRepository.findByIdNotNull().get(0);
        assertTrue(accountType != null, "Need an account type");

        Account account = Account.builder()
                .description(UUID.randomUUID().toString())
                .accountTypeId(AggregateReference.to(accountType.getId()))
                .build();

        long countBefore = accountRepository.count();
        long id = accountRepository.save(account).getId();
        long countAfter = accountRepository.count();
        assertTrue(id != 0, "ID should be non-zero after save");
        assertTrue(countAfter == countBefore + 1, "Count should have incremented by 1");

        AccountHistory accountHistory1 = AccountHistory.builder()
                .description("one")
                .build();
        AccountHistory accountHistory2 = AccountHistory.builder()
                .description("two")
                .build();
        AccountHistory accountHistory3 = AccountHistory.builder()
                .description("three")
                .build();
        account.getAccountHistories().add(accountHistory1);
        account.getAccountHistories().add(accountHistory2);
        account.getAccountHistories().add(accountHistory3);
        accountRepository.save(account);
        assertTrue(accountRepository.count() == countAfter, "Update should keep count the same");

        Optional<Account> reloaded = accountRepository.findById(id);
        assertTrue(reloaded.isPresent() && reloaded.get().getAccountHistories().size() == 3, "All history records should have persisted");
    }

}
