package com.example.myapplication.data.source.local.database.dao;

import android.database.sqlite.SQLiteException;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.source.local.projection.HabitEntyDetails;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class HabitEntyDao implements BaseDao<HabitEnty> {
    @Query("SELECT * FROM ENTY_H ")
    public abstract Flowable<List<HabitEnty>> list();

    @Query("SELECT * FROM ENTY_H WHERE id = :id ")
    public abstract Single<HabitEnty> findById(long id);

    @Query("SELECT * FROM ENTY_H WHERE categori_h = :id and :data= data ")
    public abstract Single<List<HabitEnty>> findByIdAndData(long id, LocalDate data);

    @Query("SELECT * FROM ENTY_H WHERE categori_h = :id and data BETWEEN :start AND :end ")
    public abstract Single<List<HabitEnty>> findByIdAndDataPeriod(long id, LocalDate start, LocalDate end);

    @Transaction
    @Query("SELECT * FROM ENTY_H "
            + "WHERE id = :apontamentoId ")
    public abstract Single<HabitEntyDetails> findWithDetails(long apontamentoId);

    @Query("DELETE FROM ENTY_I WHERE habit_enty = :id; ")
    abstract void deleteItens(long id);

    public void deleteEnty(HabitEnty habitEnty){
        this.deleteItens(habitEnty.getId());
        this.delete(habitEnty);
    }
}
