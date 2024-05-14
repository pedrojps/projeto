package com.example.myapplication.data.repository;

import androidx.annotation.NonNull;

import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.source.local.database.dao.HabitCategoriDao;
import com.example.myapplication.data.source.local.database.dao.HabitEntyDao;
import com.example.myapplication.data.source.local.projection.HabitEntyDetails;
import com.example.myapplication.utils.DayOfWeek;

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
    public Single<HabitEnty> findByIdEnty(long id){
        return mHabitEntyDao.findById(id);
    }

    public Single<HabitEntyDetails> findWithDetails(long id){
        return mHabitEntyDao.findWithDetails(id);
    }
    public Single<List<HabitEnty>> findByIdAndData(long id, LocalDate date){
        return mHabitEntyDao.findByIdAndData(id,date);
    }

    public Single<List<HabitEnty>> findByIdAndDataPeriod(long id, LocalDate start, LocalDate end){
        return mHabitEntyDao.findByIdAndDataPeriod(id,start,end);
    }

    public Single<List<HabitEntyDetails>> findByIdAndDataPeriodDetails(long id, LocalDate start, LocalDate end){
        return mHabitEntyDao.findByIdAndDataPeriodDetails(id,start,end);
    }

    public Flowable<? extends List<? extends HabitCategoria>> list(DayOfWeek day){
        if (day == DayOfWeek.NONE)
            return mHabitCategoriDao.list();
        else
            return mHabitCategoriDao.listByHabitAlertByDay("%" + day.getId() + "%");
    }

    public Single<HabitCategoria> insert(@NonNull HabitCategoria habitCategoria) {
        return Single.fromCallable(() -> {
            long rowId = mHabitCategoriDao.insert(habitCategoria);
            habitCategoria.setId(rowId);
            return habitCategoria;
        });
    }

    public Single<HabitEnty> insertEnty(@NonNull HabitEnty habitEnty) {
        return Single.fromCallable(() -> {
            long rowId = mHabitEntyDao.insert(habitEnty);
            habitEnty.setId(rowId);
            return habitEnty;
        });
    }

    public Single<HabitEnty> UpdateEnty(@NonNull HabitEnty habitEnty) {
        return Single.fromCallable(() -> {
            mHabitEntyDao.deleteEnty(habitEnty);
            long rowId = mHabitEntyDao.insert(habitEnty);
            habitEnty.setId(rowId);
            return habitEnty;
        });
    }

    public Completable update(@NonNull HabitCategoria habitCategoria){
        return Completable.fromAction(() -> mHabitCategoriDao.update(habitCategoria));
    }

    public Completable delete(@NonNull HabitCategoria habitCategoria){
        return Completable.fromAction(() -> mHabitCategoriDao.delete(habitCategoria));
    }

    public Completable deleteEnty(@NonNull HabitEnty habitEnty){
        return Completable.fromAction(() -> mHabitEntyDao.deleteEnty(habitEnty));
    }

    public Completable deleteAll(@NonNull List<HabitCategoria> habitCategorias){
        return Completable.fromAction(() -> mHabitCategoriDao.delete(habitCategorias));
    }


}
