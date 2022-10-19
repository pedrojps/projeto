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
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApontEquipamentoDetailViewModel extends AndroidViewModel {

    public final ObservableField<String> observacao = new ObservableField<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final SingleLiveEvent<Long> mEditApontamento = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mApontamentoDeleted = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCarregaEnty = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCarregaMes = new SingleLiveEvent<>();

    private final HabitCategoriRepository mApontEquipamentoRepository;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<HabitEnty> mListVariaveis =new ArrayList<>();

    private List<HabitEnty> mListVariaveisMes =new ArrayList<>();

    private HabitCategoria mApontamento;

    private long idHabit = 0;

    public LocalDate date = new LocalDate();

    private final SingleLiveEvent<Void> mHabitAdd = new SingleLiveEvent<>();

    public ApontEquipamentoDetailViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository apontEquipamentoRepository,
            long apontamentoId
    ) {
        super(application);
        mApontEquipamentoRepository = apontEquipamentoRepository;
       // mResourceProvider = new ResourceProvider(getApplication());
        idHabit = apontamentoId;
        loadApontamento(apontamentoId);
    }

    private void loadApontamento(long apontamentoId) {
        mApontEquipamentoRepository.findById(apontamentoId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apontWithDetails -> {
                    mApontamento= apontWithDetails;
                }, this::showError);
        loadEnty(new LocalDate());
    }

    public void loadEnty(LocalDate date){
        this.date = date;
        mApontEquipamentoRepository.findByIdAndData(idHabit,this.date)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apontWithDetails -> {
                    mListVariaveis = apontWithDetails;
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

        mApontEquipamentoRepository.findByIdAndDataPeriod(idHabit,start,new LocalDate(end.getTime()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apontWithDetails -> {
                    mListVariaveisMes = apontWithDetails;
                    mCarregaMes.call();
                }, this::showError);
    }

    public List<HabitEntyViewItem> getVariaveis() {
        List<HabitEntyViewItem> list = new ArrayList<>();
        for (HabitEnty i : mListVariaveis){
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

    public HabitCategoria getApontamento() {
        return mApontamento;
    }

    public SingleLiveEvent<Void> getHabitAdd() {
        return mHabitAdd;
    }

    public void openAddHabit() {
        mHabitAdd.call();
    }

    public SingleLiveEvent<Long> getEditApontamento() {
        return mEditApontamento;
    }

    public void editApontamento() {
        mEditApontamento.setValue(mApontamento.getId());
    }

    public SingleLiveEvent<Void> getApontamentoDeleted() {
        return mApontamentoDeleted;
    }

    public void delete() {
        mApontEquipamentoRepository.delete(mApontamento)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mApontamentoDeleted::call, this::showError);
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

        private final HabitCategoriRepository mApontEquipamentoRepository;

        private final long mApontamentoId;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository apontEquipamentoRepository,
                long apontamentoId
        ) {
            mApplication = application;
            mApontEquipamentoRepository = apontEquipamentoRepository;
            mApontamentoId = apontamentoId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ApontEquipamentoDetailViewModel(mApplication, mApontEquipamentoRepository, mApontamentoId);
        }
    }
}