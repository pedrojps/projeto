package com.example.myapplication.ui.entidadeHabitoAdd;

import android.app.Application;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.example.myapplication.data.types.TipoEquipamento;
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
import static com.example.myapplication.ui.dialog.DialogActivity.CAMINHO_KEY;
import static com.example.myapplication.ui.dialog.DialogActivity.TIPO_KEY;

public class AddEditHabitoCategoriaViewModel extends AndroidViewModel {

    public final ObservableField<String> id = new ObservableField<>();

    public final ObservableField<String> modelo = new ObservableField<>();

    public final ObservableField<String> descricao = new ObservableField<>();

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
    public final ObservableField<LocalDate> startTime = new ObservableField<>(new LocalDate());

    public AddEditHabitoCategoriaViewModel(
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

    public void start() {
        start(null);
    }

    public void start(String equipamentoId) {
        if (Strings.isNullOrEmpty(equipamentoId)) {
            mIsNewEquipamento = true;
            return;
        }

        mIsNewEquipamento = false;
        loadEquipamento(Integer.parseInt(equipamentoId));
    }

    private void loadEquipamento(@NonNull long equipamentoId) {
        mHabitCategoriRepository.findById(equipamentoId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitCategoria -> {
                    this.setEquipamento(habitCategoria);

                }, this::showError);

        mVariavelCategoriRepository.findByHabit(equipamentoId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var->{
                    this.setVariaveis(var);variavelCarrega();
                    },
                        throwable -> {setPrefixoError(R.string.carrega_habyto);});
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
        startTime.set(new LocalDate(newDate));
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

    private void setEquipamento(HabitCategoria equipamento) {
        //mHabitCategoriaOld = equipamento;

        if (equipamento != null) {
            id.set(equipamento.getId()+"");
           // modelo.set(equipamento.getNome());
            descricao.set(equipamento.getDiscricao());
        } else {
            id.set("0");
            //modelo.set(getApplication().getString(R.string.no_data_avaliable));
            descricao.set(getApplication().getString(R.string.no_data_avaliable));
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

    /**
     * O index dos botões do {@link RadioGroup} na view deve estar sincronizado com {@link TipoEquipamento}
     */
    public void onSelectProprietario(RadioGroup radioGroup, int checkedId) {
        RadioButton radioButton = radioGroup.findViewById(checkedId);
        propriedade.set(radioGroup.indexOfChild(radioButton));
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
               Integer.parseInt(this.id.get()),this.startDate.get(),new LocalTime(this.startTime.get().getTime()), modelo.get());

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
            String requisitante = data.getStringExtra(CAMINHO_KEY);

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
                   // mHabitEnty = habitEnty;
                    List<ItemEnty> lis = new ArrayList<>();
                    for (int i=0;itens.size()>i;i++){
                        ItemEnty ic = itens.get(i);
                        ic.setHabitEntyID(habitEnty.getId());
                        lis.add(ic);
                    }
                    if(lis.size()!=0)
                    mVariavelCategoriRepository.insertEnty(lis).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(()->{mEquipamentoSaved.call();},throwable -> {setPrefixoError(R.string.error_insert_equipamento_message);});
                    else
                        mEquipamentoSaved.call();
                }, throwable -> {
                    setPrefixoError(R.string.error_insert_equipamento_message);
                });
    }

    private void update(@NonNull HabitCategoria equipamento) {
        List<ItemCategoria> lis = new ArrayList<>();
        for (int i=0;mVariaveis.size()>i;i++){
            ItemCategoria ic = mVariaveis.get(i);
            ic.setCategoriID(equipamento.getId());
            if(ic.getId()==0)
                lis.add(ic);
        }

        mHabitCategoriRepository.update(equipamento).andThen(mVariavelCategoriRepository.insert(lis))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEquipamentoSaved::call, throwable -> {
                    setPrefixoError(R.string.error_insert_equipamento_message);
                });
    }

    public boolean isNewEquipamento() {
        return mIsNewEquipamento;
    }

    private void validatePrefixo(String prefixo) {
        if (Strings.isNullOrEmpty(prefixo)) {
            setPrefixoError(R.string.required_field);
        } else {
            setPrefixoError(null);
        }
    }

    private void validateDescricao(String descricao) {
        if (Strings.isNullOrEmpty(descricao)) {
            setDescricaoError(R.string.required_field);
        } else {
            setDescricaoError(null);
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

    private void setDescricaoError(@StringRes Integer errorResId) {
        if (ObjectUtils.nonNull(errorResId)) {
            descricaoError.set(getApplication().getString(errorResId));
        } else {
            descricaoError.set(null);
        }
        updateButtonState();
    }

    public void removerItens(ItemCategoria i,int posisao){
        if(!isNewEquipamento()){
            mVariavelCategoriRepository.delete(i).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(()->{
                        posisaoAdapt=(posisao);
                        mVariaveis.remove(i);
                        mDeletVariavelAntiga.call();
                    },throwable -> {setPrefixoError(R.string.error_delet_variavel_message);});;
        }else
            mVariaveis.remove(i);
    }

    public SingleLiveEvent<Integer> getDeletVariavelAntiga() {
        return mDeletVariavelAntiga;
    }

    private void setupObservables() {
        id.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String value = id.get();
                validatePrefixo(value);
            }
        });

        descricao.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String value = descricao.get();
                validateDescricao(value);
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
            return (T) new AddEditHabitoCategoriaViewModel(
                    mApplication, mHabitCategoriRepository,mVariavelCategoriRepository
            );
        }
    }
}
