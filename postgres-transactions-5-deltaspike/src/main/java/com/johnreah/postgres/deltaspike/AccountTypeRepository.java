package com.johnreah.postgres.deltaspike;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Vector;

@Repository(forEntity = AccountTypeEntity.class)
public abstract class AccountTypeRepository extends AbstractEntityRepository<AccountTypeEntity, Long> {

    public abstract List<AccountTypeEntity> findByDescription(String description);

    @Query("SELECT t, a FROM AccountTypeEntity t JOIN t.accounts a WHERE t.id = ?1")
    public abstract List<Object> findByIdWithAccounts(Long id);

}
