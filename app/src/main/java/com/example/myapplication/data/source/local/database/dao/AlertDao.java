package com.example.myapplication.data.source.local.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entities.AlertCategori;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface AlertDao extends BaseDao<AlertCategori>{

    @Query("SELECT * FROM ALERT_CATEGORI WHERE categori_h = :id ")
    Single<List<AlertCategori>> findByHabit(long id);

    @Insert(onConflict = IGNORE )
    long[] insertAll(List<AlertCategori> entity);

    @Query("DELETE FROM ALERT_CATEGORI WHERE categori_h = :idc ")
    void deleteID(long idc);

}
