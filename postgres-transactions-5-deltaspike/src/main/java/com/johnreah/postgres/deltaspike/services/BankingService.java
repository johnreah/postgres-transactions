package com.johnreah.postgres.deltaspike.services;

import com.johnreah.postgres.deltaspike.entities.AccountEntity;
import com.johnreah.postgres.deltaspike.entities.AccountTypeEntity;
import com.johnreah.postgres.deltaspike.repositories.AccountRepository;
import com.johnreah.postgres.deltaspike.repositories.AccountTypeRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

public class BankingService {

    @Inject
    AccountTypeRepository accountTypeRepository;

    @Inject
    AccountRepository accountRepository;

    public void createContrivedAccountTypeWithThreeAccountsNonTransactional(String ref1, String ref2, String ref3) {
        try {
            String newAccountTypeDecription = UUID.randomUUID().toString();
            AccountTypeEntity newAccountType = new AccountTypeEntity(newAccountTypeDecription, "new account type reference", null);
            newAccountType = accountTypeRepository.saveAndFlush(newAccountType);

            AccountEntity account1 = new AccountEntity(newAccountType, "new account 1", 100.0, Date.from(Instant.now()), ref1);
            accountRepository.saveAndFlush(account1);

            AccountEntity account2 = new AccountEntity(newAccountType, "new account 2", 200.0, Date.from(Instant.now()), ref2);
            accountRepository.saveAndFlush(account2);

            AccountEntity account3 = new AccountEntity(newAccountType, "new account 3", 300.0, Date.from(Instant.now()), ref3);
            accountRepository.saveAndFlush(account3);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }

    @Transactional
    public void createContrivedAccountTypeWithThreeAccountsTransactional(String ref1, String ref2, String ref3) {
        createContrivedAccountTypeWithThreeAccountsNonTransactional(ref1, ref2, ref3);
    }

    public long countAccountTypes() {
        return accountTypeRepository.count();
    }

    public long countAccounts() {
        return accountRepository.count();
    }

    public void deleteEverything() {
        accountRepository.findAll().stream().forEach(accountRepository::removeAndFlush);
        accountTypeRepository.findAll().stream().forEach(accountTypeRepository::removeAndFlush);
    }

}
