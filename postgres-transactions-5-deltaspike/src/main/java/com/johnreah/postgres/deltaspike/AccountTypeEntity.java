package com.johnreah.postgres.deltaspike;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="account_type"
        ,schema="public"
)
//JR
public class AccountTypeEntity  implements java.io.Serializable {


    @Id @GeneratedValue(strategy=IDENTITY)


    @Column(name="id", unique=true, nullable=false)
    private Long id;


    @Column(name="description")
    private String description;


    @Column(name="reference")
    private String reference;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="accountType")
    private Set<AccountEntity> accounts = new HashSet<AccountEntity>(0);

    public AccountTypeEntity() {
    }

    public AccountTypeEntity(String description, String reference, Set<AccountEntity> accounts) {
        this.description = description;
        this.reference = reference;
        this.accounts = accounts;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    public Set<AccountEntity> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Set<AccountEntity> accounts) {
        this.accounts = accounts;
    }




}


