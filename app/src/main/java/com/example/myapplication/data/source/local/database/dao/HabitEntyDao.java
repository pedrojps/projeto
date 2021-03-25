package com.example.myapplication.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface HabitEntyDao extends BaseDao<HabitEnty> {
    @Query("SELECT * FROM ENTY_H ")
    Flowable<List<HabitEnty>> list();

    @Query("SELECT * FROM ENTY_H WHERE id = :id ")
    Single<HabitEnty> findById(long id);
}
