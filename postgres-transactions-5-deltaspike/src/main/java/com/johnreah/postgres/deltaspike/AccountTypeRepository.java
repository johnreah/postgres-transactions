package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository(forEntity = AccountTypeEntity.class)
public abstract class AccountTypeRepository extends AbstractEntityRepository<AccountTypeEntity, Long> {

    public abstract List<AccountTypeEntity> findByDescription(String description);

}
