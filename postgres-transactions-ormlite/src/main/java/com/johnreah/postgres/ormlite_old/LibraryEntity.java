package com.johnreah.postgres.ormlite_old;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "libraries", daoClass = LibraryDaoImpl.class)
public class LibraryEntity {

    @DatabaseField(generatedId = true)
    private long libraryId;

    @DatabaseField(canBeNull = false)
    private String name;

    public LibraryEntity() {
    }

    public long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(long libraryId) {
        this.libraryId = libraryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}