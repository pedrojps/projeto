package com.example.myapplication.data.repository;

import androidx.annotation.NonNull;

import com.example.myapplication.data.entities.AlertCategori;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.source.local.database.dao.AlertDao;
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

    private final AlertDao mAlertDao;

    private VariavelCategoriRepository(VariavelCategoriDao variavelCategoriDao,VariavelEntyDao variavelEntyDao,AlertDao alertDao) {
        mVariavelCategoriDao = variavelCategoriDao;
        mVariavelEntyDao = variavelEntyDao;
        mAlertDao = alertDao;
    }

    public static synchronized VariavelCategoriRepository getInstance(@NonNull VariavelCategoriDao variavelCategoriDao,VariavelEntyDao variavelEntyDao,AlertDao alertDao){
        if(INSTANCE == null){
            INSTANCE = new VariavelCategoriRepository(variavelCategoriDao,variavelEntyDao,alertDao);
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

    public Completable insert(@NonNull ItemCategoria itemCategoria){
        return Completable.fromAction(() -> mVariavelCategoriDao.insert(itemCategoria));
    }
    public Completable insert(@NonNull List<ItemCategoria> itemCategorias){
        return Completable.fromAction(() ->
                mVariavelCategoriDao.insert(itemCategorias)
        );
    }
    public Completable insertEnty(@NonNull List<ItemEnty> itemEnties){
        return Completable.fromAction(() -> mVariavelEntyDao.insert(itemEnties));
    }

    public Completable update(@NonNull ItemCategoria itemCategoria){
        return Completable.fromAction(() -> mVariavelCategoriDao.update(itemCategoria));
    }

    public Completable Update(List<ItemCategoria> itemCategorias, long idc) {
        return Completable.fromAction(() -> {
            if (itemCategorias != null && itemCategorias.size() != 0){
                mVariavelCategoriDao.insertAll(itemCategorias);

                String[] ids = new String[itemCategorias.size()];
                for(int i = 0; i<itemCategorias.size();i++)
                    ids[i] = itemCategorias.get(i).getNome();

                mVariavelCategoriDao.deleteNotIn(ids, idc);
            }else
                mVariavelCategoriDao.deleteID(idc);
        });
    }

    public Single<List<ItemEnty>> findByHabitEnty(long id){
        return mVariavelEntyDao.findByHabit(id);
    }
    public Completable delete(@NonNull ItemCategoria itemCategoria){
        return Completable.fromAction(() -> mVariavelCategoriDao.delete(itemCategoria));
    }

    public Completable deleteAll(@NonNull List<ItemCategoria> itemCategorias){
        return Completable.fromAction(() -> mVariavelCategoriDao.delete(itemCategorias));
    }

    public Completable delete(@NonNull AlertCategori alertCategori){
        return Completable.fromAction(() -> mAlertDao.delete(alertCategori));
    }

    public Completable insertAlert(@NonNull List<AlertCategori> alertCategoris){
        return Completable.fromAction(() -> mAlertDao.insert(alertCategoris));
    }

    public Single<List<AlertCategori>> findByHabitAlert(long id){
        return mAlertDao.findByHabit(id);
    }

    public Completable updateAlert(List<AlertCategori> alertCategoris, long idc) {
        return Completable.fromAction(() -> {
            mAlertDao.deleteID(idc);
            mAlertDao.insertAll(alertCategoris);
        });
    }
}
