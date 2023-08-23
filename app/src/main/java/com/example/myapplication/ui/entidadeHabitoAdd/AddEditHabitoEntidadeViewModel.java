package com.example.myapplication.ui.entidadeHabitoAdd;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.repository.VariavelCategoriRepository;
import com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro.VarCategoriCriateViewItem;
import com.example.myapplication.utils.ObjectUtils;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.myapplication.ui.dialog.DialogActivity.NAME_KEY;
import static com.example.myapplication.ui.dialog.DialogActivity.TIPO_KEY;

public class AddEditHabitoEntidadeViewModel extends AndroidViewModel {

    public final ObservableField<String> idCategoria = new ObservableField<>();

    public final ObservableField<String> modelo = new ObservableField<>();

    public final ObservableInt propriedade = new ObservableInt(0);

    public final ObservableField<String> prefixoError = new ObservableField<>();

    public final ObservableField<String> modeloError = new ObservableField<>();

    public final ObservableField<String> descricaoError = new ObservableField<>();

    public final ObservableBoolean buttonEnabled = new ObservableBoolean();

    private final SingleLiveEvent<Void> mEquipamentoSaved = new SingleLiveEvent<>();

    private final SingleLiveEvent<Integer> mDeletVariavelAntiga = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mVariavelAdd = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mVariavelCarrega = new SingleLiveEvent<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final HabitCategoriRepository mHabitCategoriRepository;

    private final VariavelCategoriRepository mVariavelCategoriRepository;

    private List<ItemCategoria> mVariaveis=new ArrayList<>();

    private boolean mIsNewEquipamento = false;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public int posisaoAdapt= -1;

    private final SingleLiveEvent<Void> mChangeSalve = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mChangeStartTime = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mChangeStartDate = new SingleLiveEvent<>();

    public final ObservableField<LocalDate> startDate = new ObservableField<>(new LocalDate());
    public final ObservableField<LocalTime> startTime = new ObservableField<>(new LocalTime());

    private long idEdit = -1;

    public AddEditHabitoEntidadeViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository habitCategoriRepository,
            @NonNull VariavelCategoriRepository variavelCategoriRepository
    ) {
        super(application);
        mHabitCategoriRepository = habitCategoriRepository;
        mVariavelCategoriRepository = variavelCategoriRepository;
        setupObservables();
    }

    private void updateButtonState() {
        boolean enabled = Strings.isNullOrEmpty(prefixoError.get())
                && Strings.isNullOrEmpty(modeloError.get())
                && Strings.isNullOrEmpty(descricaoError.get());

        buttonEnabled.set(enabled);
    }

    public void start(String equipamentoId) {
        mIsNewEquipamento = false;
        loadCategoria(Integer.parseInt(equipamentoId));
    }

    public void start(HabitEnty habitEnty) {
        if (habitEnty == null) {
            mIsNewEquipamento = true;
            return;
        }

        mIsNewEquipamento = false;
        startDate.set(habitEnty.getData());
        startTime.set(habitEnty.getHora());
        modelo.set(habitEnty.getObservacao());
        idEdit = habitEnty.getId();
        loadCategoria(habitEnty.getCategoriaId());
        loadEdit(habitEnty.getId());
    }

    private void loadCategoria(@NonNull long equipamentoId) {
        mHabitCategoriRepository.findById(equipamentoId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitCategoria -> {
                    this.setCategoria(habitCategoria);

                }, this::showError);

        mVariavelCategoriRepository.findByHabit(equipamentoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var->{
                    this.setVariaveis(var);variavelCarrega();
                    if (!isNewEquipamento()){
                        mVariavelCategoriRepository.findByHabitEnty(idEdit)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe( e ->{
                                    setValorVariaveis(e);
                                },throwable -> {setPrefixoError(R.string.carrega_habyto);});
                    }
                    },
                        throwable -> {setPrefixoError(R.string.carrega_habyto);});
    }

    private void loadEdit(@NonNull long equipamentoId) {
        //load de entidade
    }

    private void setValorVariaveis(List<ItemEnty> iv){
        for(int i = 0; iv.size() > i; i++){
            for (int k = 0; mVariaveis.size() > k; k++){
                if (mVariaveis.get(k).getId() == iv.get(i).getItemCategoriID()){
                    mVariaveis.get(k).valor = iv.get(i).getValor();
                }
            }
        }
    }


    public void showStartDatePicker(){
        mChangeStartDate.call();
    }

    public SingleLiveEvent<Void> getChangeStartDate() {
        return mChangeStartDate;
    }
    public void setStartDate(Date newDate){
        startDate.set(new LocalDate(newDate));
    }

    public void showChangeSalve(){
        mChangeSalve.call();
    }
    public void showStartTimePicker(){
        mChangeStartTime.call();
    }
    public SingleLiveEvent<Void> getChangeSalve() {
        return mChangeSalve;
    }
    public SingleLiveEvent<Void> getChangeStartTime() {
        return mChangeStartTime;
    }
    public void setStartTime(Date newDate){
        startTime.set(new LocalTime(newDate));
    }

    public void setVariaveis(List<ItemCategoria> mVariaveis) {
        this.mVariaveis = mVariaveis;
    }

    public List<VarCategoriCriateViewItem> getVariaveis() {
        List<VarCategoriCriateViewItem> list = new ArrayList<>();
        for (ItemCategoria i : mVariaveis ){
            list.add(new VarCategoriCriateViewItem(i));
        }
        return list;
    }

    private void setCategoria(HabitCategoria equipamento) {
        //mHabitCategoriaOld = equipamento;

        if (equipamento != null) {
            idCategoria.set(equipamento.getId()+"");
        } else {
            idCategoria.set("0");
            propriedade.set(0);
        }
    }

    public SingleLiveEvent<Void> getEquipamentoSavedEvent() {
        return mEquipamentoSaved;
    }

    public SingleLiveEvent<Void> getVariaveisAdd() {
        return mVariavelAdd;
    }

    public SingleLiveEvent<Void> getVariavelCarrega() {
        return mVariavelCarrega;
    }



    public ErrorDialogMessage getErrorDialogMessage() {
        return mErrorDialogMessage;
    }

    private void showError(Throwable throwable) {
        mErrorDialogMessage.setValue("Erro", throwable.getMessage());
    }

    public void save(List<VarCategoriCriateViewItem> vari) {
        List<ItemEnty> itens  = new ArrayList<>();
        for(VarCategoriCriateViewItem v:vari){
            if(v.getModel().getTipo()==0)
                itens.add(new ItemEnty(0,v.getModel().getId(),null,v.getModel().time.get().toString()));
            else
                itens.add(new ItemEnty(0,v.getModel().getId(),null,v.getModel().valor));
        }

        HabitEnty equipamento = new HabitEnty(
               Integer.parseInt(this.idCategoria.get()),this.startDate.get(),new LocalTime(this.startTime.get().getTime()), modelo.get());

        if (idEdit>=0){
            equipamento = new HabitEnty(idEdit,
                    Integer.parseInt(this.idCategoria.get()),this.startDate.get(),new LocalTime(this.startTime.get().getTime()), modelo.get(),null);
            update(equipamento, itens);
        }

        insert(equipamento,itens);
    }

    public void variavelAdd() {
        mVariavelAdd.call();
    }
    public void variavelCarrega() {
        mVariavelCarrega.call();
    }

    public VarCategoriCriateViewItem handleEditRequisitanteResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String requisitante = data.getStringExtra(NAME_KEY);

            int tipo = Integer.parseInt(data.getStringExtra(TIPO_KEY));
            ItemCategoria variavel=new ItemCategoria(0,null,requisitante,tipo);
            mVariaveis.add(variavel);
            return new VarCategoriCriateViewItem(variavel);
        }
        return null;
    }

    private void insert(@NonNull HabitEnty equipamento,List<ItemEnty> itens) {//

       mHabitCategoriRepository.insertEnty(equipamento)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitEnty -> {
                    List<ItemEnty> lis = new ArrayList<>();
                    for (int i=0;itens.size()>i;i++){
                        ItemEnty ic = itens.get(i);
                        ic.setHabitEntyID(habitEnty.getId());
                        lis.add(ic);
                    }
                    if(lis.size()!=0)
                    mVariavelCategoriRepository.insertEnty(lis).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(()->{mEquipamentoSaved.call();},throwable -> {setPrefixoError(R.string.error_criacao_habito);});
                    else
                        mEquipamentoSaved.call();
                }, throwable -> {
                    setPrefixoError(R.string.error_criacao_habito);
                });
    }

    private void update(@NonNull HabitEnty equipamento, List<ItemEnty> itens) {


        mHabitCategoriRepository.UpdateEnty(equipamento)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitEnty -> {
                    List<ItemEnty> lis = new ArrayList<>();
                    for (int i=0;itens.size()>i;i++){
                        ItemEnty ic = itens.get(i);
                        ic.setHabitEntyID(habitEnty.getId());
                        lis.add(ic);
                    }
                    if(lis.size()!=0)
                        mVariavelCategoriRepository.insertEnty(lis).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(()->{mEquipamentoSaved.call();},throwable -> {setPrefixoError(R.string.error_criacao_habito);});
                    else
                        mEquipamentoSaved.call();
                }, throwable -> {
                    setPrefixoError(R.string.error_criacao_habito);
                });
    }

    public boolean isNewEquipamento() {
        return mIsNewEquipamento;
    }

    private void validatePrefixo(String prefixo) {
        if (Strings.isNullOrEmpty(prefixo)) {
            setPrefixoError(R.string.src_campo_obrigatorio);
        } else {
            setPrefixoError(null);
        }
    }

    private void setPrefixoError(@StringRes Integer errorResId) {
        if (ObjectUtils.nonNull(errorResId)) {
            prefixoError.set(getApplication().getString(errorResId));
        } else {
            prefixoError.set(null);
        }
        updateButtonState();
    }

    public SingleLiveEvent<Integer> getDeletVariavelAntiga() {
        return mDeletVariavelAntiga;
    }

    private void setupObservables() {
        idCategoria.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String value = idCategoria.get();
                validatePrefixo(value);
            }
        });

    }


    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
    }

    public static class Factory implements ViewModelProvider.Factory{

        private final Application mApplication;
        private final HabitCategoriRepository mHabitCategoriRepository;
        private final VariavelCategoriRepository mVariavelCategoriRepository;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository habitCategoriRepository,
                @NonNull VariavelCategoriRepository variavelCategoriRepository
        ) {
            mApplication = application;
            mHabitCategoriRepository = habitCategoriRepository;
            mVariavelCategoriRepository = variavelCategoriRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AddEditHabitoEntidadeViewModel(
                    mApplication, mHabitCategoriRepository,mVariavelCategoriRepository
            );
        }
    }
}
