package com.example.myapplication.data.source.local.database.dao;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<T> {

    @Insert
    long insert(T entity);

    @Insert
    long[] insert(List<T> entity);

    @Update
    int update(T entity);

    @Update
    int update(List<T> entity);

    @Delete
    void delete(T entity);

    @Delete
    void delete(List<T> entity);

}
