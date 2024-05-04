package com.example.myapplication.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.myapplication.data.entities.HabitCategoria;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface HabitCategoriDao extends BaseDao<HabitCategoria>{

    @Query("SELECT * FROM CATEGORIA_H WHERE nome IS NOT NULL")
    Flowable<List<HabitCategoria>> list();

    @Query("SELECT * FROM CATEGORIA_H WHERE id = :id ")
    Single<HabitCategoria> findById(long id);

    @Query("SELECT * FROM CATEGORIA_H WHERE nome IS NOT NULL and day_of_week LIKE :day")
    Flowable<List<HabitCategoria>> listByDayOfWeek(String day);

}
