package com.example.myapplication.data.repository;

import androidx.annotation.NonNull;

import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.source.local.database.dao.HabitCategoriDao;
import com.example.myapplication.data.source.local.database.dao.HabitEntyDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class HabitCategoriRepository {

    private static HabitCategoriRepository INSTANCE;

    private final HabitCategoriDao mHabitCategoriDao;

    private final HabitEntyDao mHabitEntyDao;

    private HabitCategoriRepository(HabitCategoriDao habitCategoriDao,HabitEntyDao habitEntyDao) {
        mHabitCategoriDao = habitCategoriDao;
        mHabitEntyDao =habitEntyDao;
    }

    public static synchronized HabitCategoriRepository getInstance(@NonNull HabitCategoriDao projetoDao,HabitEntyDao habitEntyDao){
        if(INSTANCE == null){
            INSTANCE = new HabitCategoriRepository(projetoDao,habitEntyDao);
        }
        return INSTANCE;
    }

    public void clearInstance(){
        INSTANCE = null;
    }

    public Single<HabitCategoria> findById(long id){
        return mHabitCategoriDao.findById(id);
    }

    public Flowable<List<HabitCategoria>> list(){
        return mHabitCategoriDao.list();
    }

    //public Completable insert(@NonNull HabitCategoria projeto){
   //     return Completable.fromAction(() -> mProjetoDao.insert(projeto));
    //}

    public Single<HabitCategoria> insert(@NonNull HabitCategoria habitCategoria) {
        return Single.fromCallable(() -> {
            long rowId = mHabitCategoriDao.insert(habitCategoria);
            habitCategoria.setId(rowId);
            return habitCategoria;
        });
    }

    public Single<HabitEnty> insertEnty(@NonNull HabitEnty habitCategoria) {
        return Single.fromCallable(() -> {
            long rowId = mHabitEntyDao.insert(habitCategoria);
            habitCategoria.setId(rowId);
            return habitCategoria;
        });
    }

    public Completable update(@NonNull HabitCategoria projeto){
        return Completable.fromAction(() -> mHabitCategoriDao.update(projeto));
    }

    public Completable delete(@NonNull HabitCategoria projeto){
        return Completable.fromAction(() -> mHabitCategoriDao.delete(projeto));
    }

    public Completable deleteAll(@NonNull List<HabitCategoria> projetos){
        return Completable.fromAction(() -> mHabitCategoriDao.delete(projetos));
    }


}
