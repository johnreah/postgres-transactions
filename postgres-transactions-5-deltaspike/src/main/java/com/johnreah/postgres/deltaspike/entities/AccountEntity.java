package com.johnreah.postgres.deltaspike.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="account", schema="public")
public class AccountEntity  implements java.io.Serializable {


    @Id @GeneratedValue(strategy=IDENTITY)


    @Column(name="id", unique=true, nullable=false)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_type_id")
    private AccountTypeEntity accountType;


    @Column(name="description")
    private String description;


    @Column(name="balance", precision=17, scale=17)
    private Double balance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="balance_timestamp", length=35)
    private Date balanceTimestamp;


    @Column(name="reference", unique = true)
    private String reference;

//    @OneToMany(fetch=FetchType.LAZY, mappedBy="account")
//    private Set<LinkCustomerAccount> linkCustomerAccounts = new HashSet<LinkCustomerAccount>(0);
//
//    @OneToMany(fetch=FetchType.LAZY, mappedBy="account")
//    private Set<AccountHistory> accountHistories = new HashSet<AccountHistory>(0);

    public AccountEntity() {
    }

    public AccountEntity(AccountTypeEntity accountType, String description, Double balance, Date balanceTimestamp, String reference /*, Set<LinkCustomerAccount> linkCustomerAccounts, Set<AccountHistory> accountHistories*/) {
        this.accountType = accountType;
        this.description = description;
        this.balance = balance;
        this.balanceTimestamp = balanceTimestamp;
        this.reference = reference;
//        this.linkCustomerAccounts = linkCustomerAccounts;
//        this.accountHistories = accountHistories;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public AccountTypeEntity getAccountType() {
        return this.accountType;
    }

    public void setAccountType(AccountTypeEntity accountType) {
        this.accountType = accountType;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Date getBalanceTimestamp() {
        return this.balanceTimestamp;
    }

    public void setBalanceTimestamp(Date balanceTimestamp) {
        this.balanceTimestamp = balanceTimestamp;
    }
    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
//    public Set<LinkCustomerAccount> getLinkCustomerAccounts() {
//        return this.linkCustomerAccounts;
//    }
//
//    public void setLinkCustomerAccounts(Set<LinkCustomerAccount> linkCustomerAccounts) {
//        this.linkCustomerAccounts = linkCustomerAccounts;
//    }
//    public Set<AccountHistory> getAccountHistories() {
//        return this.accountHistories;
//    }
//
//    public void setAccountHistories(Set<AccountHistory> accountHistories) {
//        this.accountHistories = accountHistories;
//    }




}


