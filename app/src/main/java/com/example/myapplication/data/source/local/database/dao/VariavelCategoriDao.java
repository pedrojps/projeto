package com.example.myapplication.data.source.local.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entities.ItemCategoria;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
@Dao
public interface VariavelCategoriDao extends BaseDao<ItemCategoria>{

    @Query("SELECT * FROM categoria_i")
    Flowable<List<ItemCategoria>> list();

    @Query("SELECT * FROM categoria_i WHERE id = :id ")
    Single<ItemCategoria> findById(long id);

    @Query("SELECT * FROM categoria_i WHERE categori_h = :id ")
    Single<List<ItemCategoria>> findByHabit(long id);

    @Query("DELETE FROM categoria_i WHERE categori_h = :idc and NOT (nome IN (:black))")
    void deleteNotIn(String[] black, long idc);

    @Query("DELETE FROM categoria_i WHERE categori_h = :idc ")
    void deleteID(long idc);

    @Insert(onConflict = IGNORE )
    long[] insertAll(List<ItemCategoria> entity);

}
