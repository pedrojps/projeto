package com.example.myapplication.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface VariavelEntyDao extends BaseDao<ItemEnty> {

    @Query("SELECT * FROM ENTY_I")
    Flowable<List<ItemEnty>> list();

    @Query("SELECT * FROM ENTY_I WHERE id = :id ")
    Single<ItemEnty> findById(long id);

    @Query("SELECT * FROM ENTY_I WHERE habit_enty = :id ")
    Single<List<ItemEnty>> findByHabit(long id);
}
