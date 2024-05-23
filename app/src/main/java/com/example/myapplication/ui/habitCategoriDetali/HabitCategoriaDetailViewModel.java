package com.example.myapplication.ui.habitCategoriDetali;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.ui.entidadeHabitoAdd.HabitEntyViewItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HabitCategoriaDetailViewModel extends AndroidViewModel {

    public final ObservableField<String> observacao = new ObservableField<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final SingleLiveEvent<HabitCategoria> mEditHabit = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mDeleted = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCarregaEnty = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCarregaMes = new SingleLiveEvent<>();

    private final HabitCategoriRepository mCategoriRepository;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<HabitEnty> mListVariaveis =new ArrayList<>();

    private List<HabitEnty> mListVariaveisMes =new ArrayList<>();

    private HabitCategoria mHabitCategoria;

    private long idHabit = 0;

    public LocalDate date = new LocalDate();

    private final SingleLiveEvent<Void> mHabitAdd = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mGrafic = new SingleLiveEvent<>();

    public HabitCategoriaDetailViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository categoriRepository,
            HabitCategoria habitCategoria
    ) {
        super(application);
        this.mCategoriRepository = categoriRepository;
        idHabit = habitCategoria.getId();
        mHabitCategoria = habitCategoria;
        loadEnty(new LocalDate());
    }

    public void loadEnty(LocalDate date){
        this.date = date;
        mCategoriRepository.findByIdAndData(idHabit,this.date)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(withDetails -> {
                    mListVariaveis = withDetails;
                    mCarregaEnty.call();
                }, this::showError);
    }

    public void loadEntyStart(LocalDate date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int YEAR = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
        LocalDate start = new LocalDate(calendar.getTime());


        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar end =  Calendar.getInstance();
        end.set(YEAR,month,day);

        mCategoriRepository.findByIdAndDataPeriod(idHabit,start,new LocalDate(end.getTime()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(withDetails -> {
                    mListVariaveisMes = withDetails;
                    mCarregaMes.call();
                }, this::showError);
        loadEnty(date);
    }

    public List<HabitEntyViewItem> getVariaveis() {
        List<HabitEntyViewItem> list = new ArrayList<>();
        for (HabitEnty i : mListVariaveis){
            //i.setIcone(mHabitCategoria.getIcone());
            list.add(new HabitEntyViewItem(i));
        }
        return list;
    }

    public List<HabitEnty> getVariaveisMes() {
        return mListVariaveisMes;
    }


    public  SingleLiveEvent<Void> getCarregaEnty(){
        return mCarregaEnty;
    }

    public  SingleLiveEvent<Void> getCarregaMes(){
        return mCarregaMes;
    }

    public HabitCategoria getHabit() {
        return mHabitCategoria;
    }

    public SingleLiveEvent<Void> getHabitAdd() {
        return mHabitAdd;
    }

    public void openAddHabit() {
        mHabitAdd.call();
    }

    public SingleLiveEvent<Void> getGrafic() {
        return mGrafic;
    }

    public void openGrafic() {
        mGrafic.call();
    }

    public SingleLiveEvent<HabitCategoria> getEditHabit() {
        return mEditHabit;
    }

    public void editHabit() {
        mEditHabit.setValue(mHabitCategoria);
    }

    public SingleLiveEvent<Void> getHabitDeleted() {
        return mDeleted;
    }

    public void delete() {
        mCategoriRepository.delete(mHabitCategoria)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDeleted::call, this::showError);
    }

    public ErrorDialogMessage getErrorDialogMessage(){
        return mErrorDialogMessage;
    }

    private void showError(Throwable throwable) {
        mErrorDialogMessage.setValue("Erro", throwable.getMessage());
    }

    private void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;

        private final HabitCategoriRepository mHabitRepository;

        private final HabitCategoria mHabitCategoria;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository habitCategoriRepository,
                HabitCategoria habitCategoria
        ) {
            mApplication = application;
            mHabitRepository = habitCategoriRepository;
            mHabitCategoria = habitCategoria;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new HabitCategoriaDetailViewModel(mApplication, mHabitRepository, mHabitCategoria);
        }
    }
}
