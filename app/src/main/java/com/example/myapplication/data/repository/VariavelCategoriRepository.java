package com.example.myapplication.data.repository;

import androidx.annotation.NonNull;

import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.source.local.database.dao.VariavelCategoriDao;
import com.example.myapplication.data.source.local.database.dao.VariavelEntyDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class VariavelCategoriRepository {
    private static VariavelCategoriRepository INSTANCE;

    private final VariavelCategoriDao mVariavelCategoriDao;

    private final VariavelEntyDao mVariavelEntyDao;

    private VariavelCategoriRepository(VariavelCategoriDao variavelCategoriDao,VariavelEntyDao variavelEntyDao) {
        mVariavelCategoriDao = variavelCategoriDao;
        mVariavelEntyDao = variavelEntyDao;
    }

    public static synchronized VariavelCategoriRepository getInstance(@NonNull VariavelCategoriDao variavelCategoriDao,VariavelEntyDao variavelEntyDao){
        if(INSTANCE == null){
            INSTANCE = new VariavelCategoriRepository(variavelCategoriDao,variavelEntyDao);
        }
        return INSTANCE;
    }

    public void clearInstance(){
        INSTANCE = null;
    }

    public Single<ItemCategoria> findById(long id){
        return mVariavelCategoriDao.findById(id);
    }

    public Single<List<ItemCategoria>> findByHabit(long id){
        return mVariavelCategoriDao.findByHabit(id);
    }

    public Flowable<List<ItemCategoria>> list(){
        return mVariavelCategoriDao.list();
    }

    public Completable insert(@NonNull ItemCategoria projeto){
        return Completable.fromAction(() -> mVariavelCategoriDao.insert(projeto));
    }
    public Completable insert(@NonNull List<ItemCategoria> projeto){
        return Completable.fromAction(() -> mVariavelCategoriDao.insert(projeto));
    }
    public Completable insertEnty(@NonNull List<ItemEnty> projeto){
        return Completable.fromAction(() -> mVariavelEntyDao.insert(projeto));
    }

    public Completable update(@NonNull ItemCategoria projeto){
        return Completable.fromAction(() -> mVariavelCategoriDao.update(projeto));
    }

    public Completable delete(@NonNull ItemCategoria projeto){
        return Completable.fromAction(() -> mVariavelCategoriDao.delete(projeto));
    }

    public Completable deleteAll(@NonNull List<ItemCategoria> projetos){
        return Completable.fromAction(() -> mVariavelCategoriDao.delete(projetos));
    }
}
