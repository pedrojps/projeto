package com.example.myapplication.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.source.local.projection.HabitAlertProject;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface HabitCategoriDao extends BaseDao<HabitCategoria>{

    @Query("SELECT * FROM CATEGORIA_H WHERE nome IS NOT NULL ORDER BY CATEGORIA_H.nome")
    Flowable<List<HabitCategoria>> list();

    @Query("SELECT * FROM CATEGORIA_H WHERE id = :id ")
    Single<HabitCategoria> findById(long id);

    @Query("SELECT * FROM CATEGORIA_H WHERE nome IS NOT NULL and day_of_week LIKE :day")
    Flowable<List<HabitCategoria>> listByDayOfWeek(String day);

    @Query(" SELECT CATEGORIA_H.* , ALERT_CATEGORI.id as alert_id, ALERT_CATEGORI.day_of_week as week, ALERT_CATEGORI.isAtive, ALERT_CATEGORI.time " +
            "FROM CATEGORIA_H INNER JOIN ALERT_CATEGORI ON CATEGORIA_H.id = ALERT_CATEGORI.categori_h WHERE\n" +
            " ALERT_CATEGORI.isAtive = :isAtive;")
    List<HabitAlertProject> listByHabitAlertIsAtive(boolean isAtive);

    @Query(" SELECT CATEGORIA_H.* , ALERT_CATEGORI.id as alert_id, ALERT_CATEGORI.day_of_week as week, ALERT_CATEGORI.isAtive, ALERT_CATEGORI.time " +
            "FROM CATEGORIA_H INNER JOIN ALERT_CATEGORI ON CATEGORIA_H.id = ALERT_CATEGORI.categori_h WHERE ALERT_CATEGORI.day_of_week LIKE :day " +
            "ORDER BY ALERT_CATEGORI.time" )
    Flowable<List<HabitAlertProject>> listByHabitAlertByDay(String day);
}
