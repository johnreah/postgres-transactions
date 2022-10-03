package com.johnreah.postgrestransactions6springdatajdbc.support;

import com.johnreah.postgrestransactions6springdatajdbc.entities.Account;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseUtilsHelperRepository extends CrudRepository<Account, Long> {

    @Modifying
    @Query("delete from link_customer_account")
    public void deleteLinkCustomerAccount();

    @Modifying
    @Query("delete from account_history")
    public void deleteAccountHistory();

    @Modifying
    @Query("delete from account")
    public void deleteAccount();

    @Modifying
    @Query("delete from customer")
    public void deleteCustomer();

    @Modifying
    @Query("delete from account_type")
    public void deleteAccountType();

}
