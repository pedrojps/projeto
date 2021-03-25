package com.example.myapplication.ui.addEdithabit;

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
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.repository.VariavelCategoriRepository;
import com.example.myapplication.data.types.TipoEquipamento;
import com.example.myapplication.ui.variaeisCategoriy.VarCategoriViewItem;
import com.example.myapplication.utils.ObjectUtils;
import com.google.common.base.Strings;

import java.util.ArrayList;
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

    private HabitCategoria mHabitCategoriaOld;

    private List<ItemCategoria> mVariaveis=new ArrayList<>();

    private boolean mIsNewEquipamento = false;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public int posisaoAdapt= -1;

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
                .subscribe(var->{this.setVariaveis(var);variavelCarrega();},throwable -> {setPrefixoError(R.string.carrega_habyto);});
    }

    public void setVariaveis(List<ItemCategoria> mVariaveis) {
        this.mVariaveis = mVariaveis;
    }

    public List<VarCategoriViewItem> getVariaveis() {
        List<VarCategoriViewItem> list = new ArrayList<>();
        for (ItemCategoria i : mVariaveis ){
            list.add(new VarCategoriViewItem(i));
        }
        return list;
    }

    private void setEquipamento(HabitCategoria equipamento) {
        mHabitCategoriaOld = equipamento;

        if (equipamento != null) {
            id.set(equipamento.getId()+"");
            modelo.set(equipamento.getNome());
            descricao.set(equipamento.getDiscricao());
        } else {
            id.set("0");
            modelo.set(getApplication().getString(R.string.no_data_avaliable));
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

    public void save() {
        HabitCategoria equipamento = new HabitCategoria(
               0, modelo.get(), new LocalDate() ,descricao.get(), null);

        if (isNewEquipamento()) {
            insert(equipamento);
        } else {
            equipamento.setId(Integer.parseInt(id.get()));
            update(equipamento);
        }
    }

    public void variavelAdd() {
        mVariavelAdd.call();
    }
    public void variavelCarrega() {
        mVariavelCarrega.call();
    }

    public VarCategoriViewItem handleEditRequisitanteResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String requisitante = data.getStringExtra(CAMINHO_KEY);

            int tipo = Integer.parseInt(data.getStringExtra(TIPO_KEY));
            ItemCategoria variavel=new ItemCategoria(0,null,requisitante,tipo);
            mVariaveis.add(variavel);
            return new VarCategoriViewItem(variavel);
        }
        return null;
    }

    private void insert(@NonNull HabitCategoria equipamento) {//

       mHabitCategoriRepository.insert(equipamento)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitCategoria -> {
                    mHabitCategoriaOld = habitCategoria;
                    List<ItemCategoria> lis = new ArrayList<>();
                    for (int i=0;mVariaveis.size()>i;i++){
                        ItemCategoria ic = mVariaveis.get(i);
                        ic.setCategoriID(habitCategoria.getId());
                        lis.add(ic);
                    }
                    if(lis.size()!=0)
                    mVariavelCategoriRepository.insert(lis).subscribeOn(Schedulers.io())
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
