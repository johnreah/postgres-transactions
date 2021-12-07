package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository(forEntity = AccountEntity.class)
public abstract class AccountRepository extends AbstractEntityRepository<AccountEntity, Long> {

//    public

}
