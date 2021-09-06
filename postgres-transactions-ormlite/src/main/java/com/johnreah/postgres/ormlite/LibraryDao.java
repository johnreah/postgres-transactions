package com.johnreah.postgres.ormlite;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface LibraryDao extends Dao<LibraryEntity,Long> {

    public List<LibraryEntity> findByName(String name) throws SQLException;

}
