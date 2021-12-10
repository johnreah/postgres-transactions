package com.johnreah.postgres.deltaspike.repositories;

import com.johnreah.postgres.deltaspike.entities.AccountEntity;
import com.johnreah.postgres.deltaspike.entities.AccountTypeEntity;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository(forEntity = AccountEntity.class)
public abstract class AccountRepository extends AbstractEntityRepository<AccountEntity, Long> {

    public abstract List<AccountEntity> findByAccountType(AccountTypeEntity accountTypeEntity);

}
