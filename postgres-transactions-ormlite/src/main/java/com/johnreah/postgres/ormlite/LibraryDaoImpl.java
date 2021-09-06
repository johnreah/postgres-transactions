package com.johnreah.postgres.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class LibraryDaoImpl extends BaseDaoImpl<LibraryEntity, Long> implements LibraryDao {

    public LibraryDaoImpl(ConnectionSource connectionSource, Class<LibraryEntity> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public List<LibraryEntity> findByName(String name) throws SQLException {
        return null;
    }

}
