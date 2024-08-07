package com.example.myapplication.ui.habitEntyDetali;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.source.local.projection.HabitEntyDetails;
import com.example.myapplication.data.source.local.projection.ItensEntyProject;
import com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe.VarCategoriDetalheViewItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HabitEntyDetailViewModel extends AndroidViewModel {

    public final ObservableField<String> observacao = new ObservableField<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final SingleLiveEvent<Long> mEdit = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mDeleted = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCarregaEnty = new SingleLiveEvent<>();

    private final HabitCategoriRepository mHabitCategoriRepository;

    public final ObservableField<LocalDate> startDate = new ObservableField<>(new LocalDate());

    public final ObservableField<LocalTime> startTime = new ObservableField<>(new LocalTime());

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<ItensEntyProject> mVariaveis=new ArrayList<>();

    private List<ItemEnty> mValores=new ArrayList<>();

    private HabitEntyDetails mHabitEntyDetails;

    private long idHabit = 0;

    public HabitEntyDetailViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository habitCategoriRepository,
           // @NonNull VariavelCategoriRepository variavelCategoriRepository,
            long habitEntyId
    ) {
        super(application);
        mHabitCategoriRepository = habitCategoriRepository;
        //mVariavelCategoriRepository = variavelCategoriRepository;
        idHabit = habitEntyId;
    }

    void loadHabitEnty() {
        mHabitCategoriRepository.findWithDetails(idHabit)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habiyEntyWithDetails -> {
                    setEnty(habiyEntyWithDetails);
                }, this::showError);
    }

    public void setEnty(HabitEntyDetails habiyEntyWithDetails){
        mHabitEntyDetails = habiyEntyWithDetails;
        startDate.set(habiyEntyWithDetails.habitEnty.getData());
        startTime.set(habiyEntyWithDetails.habitEnty.getHora());
        observacao.set(habiyEntyWithDetails.habitEnty.getObservacao());
        mVariaveis= habiyEntyWithDetails.itensEntyProjects;
        mCarregaEnty.call();
    }

    public List<VarCategoriDetalheViewItem> getVariaveis() {
        List<VarCategoriDetalheViewItem> list = new ArrayList<>();
        for (ItensEntyProject i : mHabitEntyDetails.itensEntyProjects){
            ItemCategoria c = i.getItemEnty();
            c.valor = i.itemEnty.getValor();
            if(c.getTipo()==0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                Date date = null;
                try {
                    date = dateFormat.parse(i.itemEnty.getValor());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.time.set(new LocalTime(date));
            }
            list.add(new VarCategoriDetalheViewItem(c));
        }
        return list;
    }

    public  SingleLiveEvent<Void> getCarregaEnty(){
        return mCarregaEnty;
    }
    public HabitEnty getHabitEnty() {
        return mHabitEntyDetails.habitEnty;
    }

    public SingleLiveEvent<Long> getEdit() {
        return mEdit;
    }

    public void edit() {
        mEdit.setValue(mHabitEntyDetails.habitEnty.getId());
    }

    public SingleLiveEvent<Void> getDeleted() {
        return mDeleted;
    }

    public void delete() {
        mHabitCategoriRepository.deleteEnty(this.mHabitEntyDetails.habitEnty)
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

        private final HabitCategoriRepository mRepository;

        private final long mId;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository repository,
                long id
        ) {
            mApplication = application;
            mRepository = repository;
            mId = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new HabitEntyDetailViewModel(mApplication, mRepository, mId);
        }
    }
}
