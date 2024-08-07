package com.example.myapplication.ui.addEdithabit;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.AlertCategori;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.repository.VariavelCategoriRepository;
import com.example.myapplication.ui.variaeisCategoriy.VarCategoriViewItem;
import com.example.myapplication.utils.ImageUtil;
import com.example.myapplication.utils.ObjectUtils;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.myapplication.ui.dialog.DialogAddHabitItemActivity.NAME_KEY;
import static com.example.myapplication.ui.dialog.DialogAddHabitItemActivity.TIPO_KEY;

public class AddEditHabitoCategoriaViewModel extends AndroidViewModel {

    public final ObservableField<String> id = new ObservableField<>();

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableField<String> descricao = new ObservableField<>();

    public final ObservableField<String> nameError = new ObservableField<>();

    public final ObservableField<String> descricaoError = new ObservableField<>();

    public final ObservableField<Bitmap> image = new ObservableField<>();

    public final ObservableBoolean buttonEnabled = new ObservableBoolean();

    private final SingleLiveEvent<Void> mCategoriaSaved = new SingleLiveEvent<>();

    private final SingleLiveEvent<Integer> mDeletVariavelAntiga = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mVariavelAdd = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mVariavelCarrega = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mFalha = new SingleLiveEvent<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final HabitCategoriRepository mHabitCategoriRepository;

    private final VariavelCategoriRepository mVariavelCategoriRepository;

    private List<ItemCategoria> mVariaveis = new ArrayList<>();
    private List<AlertCategori> mAlert = new ArrayList<>();

    private boolean mIsNovaCategoria = false;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public int posisaoAdapt= -1;

    private ArrayList<Integer> mDayOfWeek = new ArrayList();

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
        boolean enabled = Strings.isNullOrEmpty(nameError.get())
                //&& Strings.isNullOrEmpty(descricaoError.get())
                ;

        buttonEnabled.set(enabled);
    }

    public void start() {
        start(null);
    }

    public void start(HabitCategoria habitCategoria) {
        if (habitCategoria == null) {
            mIsNovaCategoria = true;
            return;
        }

        mIsNovaCategoria = false;
        load(habitCategoria.getId());
        this.setCAtegoria(habitCategoria);
    }

    private void load(@NonNull long categoriaId) {
        mVariavelCategoriRepository.findByHabit(categoriaId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var->{
                    this.setVariaveis(var);
                    variavelCarrega();},throwable -> {mFalha.call();});

        mVariavelCategoriRepository.findByHabitAlert(categoriaId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(var->{
                    this.setAlert(var);
                    variavelCarrega();},throwable -> {mFalha.call();});
    }

    public void setVariaveis(List<ItemCategoria> mVariaveis) {
        this.mVariaveis = mVariaveis;
    }

    public void setAlert(List<AlertCategori> mAlert) {
        this.mAlert = mAlert;
    }

    public List<AlertCategori> getAlert() {
        return mAlert;
    }

    public List<VarCategoriViewItem> getVariaveis() {
        List<VarCategoriViewItem> list = new ArrayList<>();
        for (ItemCategoria i : mVariaveis ){
            list.add(new VarCategoriViewItem(i));
        }
        return list;
    }

    private void setCAtegoria(HabitCategoria habitCategoria) {
        id.set(habitCategoria.getId()+"");
        name.set(habitCategoria.getNome());
        descricao.set(habitCategoria.getDiscricao());
        image.set(ImageUtil.INSTANCE.readBtn(getApplication().getBaseContext(), id.get()));
    }

    public SingleLiveEvent<Void> getSavedEvent() {
        return mCategoriaSaved;
    }

    public SingleLiveEvent<Void> getVariaveisAdd() {
        return mVariavelAdd;
    }

    public SingleLiveEvent<Void> getVariavelCarrega() {
        return mVariavelCarrega;
    }

    public SingleLiveEvent<Void> getFalha() {
        return mFalha;
    }

    public ErrorDialogMessage getErrorDialogMessage() {
        return mErrorDialogMessage;
    }

    private void showError(Throwable throwable) {
        mErrorDialogMessage.setValue("Erro", throwable.getMessage());
    }

    public void save() {
        HabitCategoria categoria = new HabitCategoria(
               0, name.get(), new LocalDate() ,descricao.get(),mDayOfWeek.toString(),  null);

        if (isNew()) {
            insert(categoria);
        } else {
            categoria.setId(Integer.parseInt(id.get()));
            update(categoria);
        }
    }

    private void salveImage(long id){
        ImageUtil.INSTANCE.saveImage(getApplication().getBaseContext(), image.get() ,id+"");
    }

    public void variavelAdd() {
        mVariavelAdd.call();
    }
    public void variavelCarrega() {
        mVariavelCarrega.call();
    }

    public VarCategoriViewItem handleEditRequisitanteResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String requisitante = data.getStringExtra(NAME_KEY);

            int tipo = Integer.parseInt(data.getStringExtra(TIPO_KEY));
            ItemCategoria variavel=new ItemCategoria(0,null,requisitante,tipo);
            mVariaveis.add(variavel);
            return new VarCategoriViewItem(variavel);
        }
        return null;
    }

    private void insert(@NonNull HabitCategoria categoria) {//

       mHabitCategoriRepository.insert(categoria)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(habitCategoria -> {
                    salveImage(habitCategoria.getId());
                    insertVariavelAlert(habitCategoria.getId());
                }, throwable -> {
                    showError(throwable);
                });
    }

    public void insertVariavelAlert(long id){
        mAlert.forEach(i-> i.setCategoriID(id));
        mVariaveis.forEach(i-> i.setCategoriID(id));

        mVariavelCategoriRepository.insert(mVariaveis).andThen(mVariavelCategoriRepository.insertAlert(mAlert))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    mCategoriaSaved.call();}, throwable -> {showError(throwable);});
    }

    private void update(@NonNull HabitCategoria habit) {
        salveImage(habit.getId());
        mAlert.forEach(i-> i.setCategoriID(habit.getId()));
        mVariaveis.forEach(i-> i.setCategoriID(habit.getId()));

        mHabitCategoriRepository.update(habit)
                .andThen(mVariavelCategoriRepository.Update(mVariaveis, habit.getId()))
                .andThen(mVariavelCategoriRepository.updateAlert(mAlert,habit.getId()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCategoriaSaved::call, throwable -> {
                    showError(throwable);
                });
    }

    public boolean isNew() {
        return mIsNovaCategoria;
    }

    private void validateName(String descricao) {
        if (Strings.isNullOrEmpty(descricao)) {
            setNameError(R.string.src_campo_obrigatorio);
        } else {
            setNameError(null);
        }
    }

    private void setNameError(@StringRes Integer errorResId) {
        if (ObjectUtils.nonNull(errorResId)) {
            nameError.set(getApplication().getString(errorResId));
        } else {
            nameError.set(null);
        }
        updateButtonState();
    }

    public void removerItens(ItemCategoria i,int posisao){
        if(!isNew() && false){
            mVariavelCategoriRepository.delete(i).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(()->{
                        posisaoAdapt=(posisao);
                        mVariaveis.remove(i);
                        mDeletVariavelAntiga.call();
                    },throwable -> {showError(throwable);});;
        }else {
            posisaoAdapt=(posisao);
            mVariaveis.remove(i);
            mDeletVariavelAntiga.call();
        }
    }

    public SingleLiveEvent<Integer> getDeletVariavelAntiga() {
        return mDeletVariavelAntiga;
    }

    private void setupObservables() {
        name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String value = name.get();
                validateName(value);
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
