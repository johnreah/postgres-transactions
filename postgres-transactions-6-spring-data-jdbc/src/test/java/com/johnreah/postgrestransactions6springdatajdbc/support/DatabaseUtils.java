package com.johnreah.postgrestransactions6springdatajdbc.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUtils {

    @Autowired
    private DatabaseUtilsHelperRepository databaseUtilsHelperRepository;

    public void deleteEverything() {
        databaseUtilsHelperRepository.deleteLinkCustomerAccount();
        databaseUtilsHelperRepository.deleteAccountHistory();
        databaseUtilsHelperRepository.deleteCustomer();
        databaseUtilsHelperRepository.deleteAccount();
        databaseUtilsHelperRepository.deleteAccountType();
    }

}
