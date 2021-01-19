package com.example.myapplication.data.repository;

import androidx.annotation.NonNull;

import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.source.local.database.dao.HabitCategoriDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class HabitCategoriRepository {

    private static HabitCategoriRepository INSTANCE;

    private final HabitCategoriDao mProjetoDao;

    private HabitCategoriRepository(HabitCategoriDao projetoDao) {
        mProjetoDao = projetoDao;
    }

    public static synchronized HabitCategoriRepository getInstance(@NonNull HabitCategoriDao projetoDao){
        if(INSTANCE == null){
            INSTANCE = new HabitCategoriRepository(projetoDao);
        }
        return INSTANCE;
    }

    public void clearInstance(){
        INSTANCE = null;
    }

    public Single<HabitCategoria> findById(long id){
        return mProjetoDao.findById(id);
    }

    public Flowable<List<HabitCategoria>> list(){
        return mProjetoDao.list();
    }

    public Completable insert(@NonNull HabitCategoria projeto){
        return Completable.fromAction(() -> mProjetoDao.insert(projeto));
    }

    public Completable update(@NonNull HabitCategoria projeto){
        return Completable.fromAction(() -> mProjetoDao.update(projeto));
    }

    public Completable delete(@NonNull HabitCategoria projeto){
        return Completable.fromAction(() -> mProjetoDao.delete(projeto));
    }

    public Completable deleteAll(@NonNull List<HabitCategoria> projetos){
        return Completable.fromAction(() -> mProjetoDao.delete(projetos));
    }


}
