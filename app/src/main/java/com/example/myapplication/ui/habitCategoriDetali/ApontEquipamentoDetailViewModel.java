package com.example.myapplication.ui.habitCategoriDetali;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.repository.HabitCategoriRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApontEquipamentoDetailViewModel extends AndroidViewModel {

    public final ObservableField<String> descricaoEquipamento = new ObservableField<>();

    public final ObservableField<String> noteEquipamento = new ObservableField<>();

    public final ObservableField<LocalDate> dataApontamento = new ObservableField<>();

    public final ObservableDouble chuva = new ObservableDouble();

    public final ObservableDouble quebra = new ObservableDouble();

    public final ObservableDouble faltaFrente = new ObservableDouble();

    public final ObservableDouble faltaOperador = new ObservableDouble();

    public final ObservableDouble manutencao = new ObservableDouble();

    public final ObservableDouble equipeIncompleta = new ObservableDouble();

    public final ObservableField<String> observacao = new ObservableField<>();

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final SingleLiveEvent<Long> mEditApontamento = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mApontamentoDeleted = new SingleLiveEvent<>();

    private final HabitCategoriRepository mApontEquipamentoRepository;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

   // private final ResourceProvider mResourceProvider;

    private HabitCategoria mApontamento;

    private final SingleLiveEvent<Void> mHabitAdd = new SingleLiveEvent<>();

    public ApontEquipamentoDetailViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository apontEquipamentoRepository,
            long apontamentoId
    ) {
        super(application);
        mApontEquipamentoRepository = apontEquipamentoRepository;
       // mResourceProvider = new ResourceProvider(getApplication());
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
/*
    private void setEquipamento(C equipamento) {
        if (equipamento != null) {
            prefixoEquipamento.set(equipamento.getId());
            descricaoEquipamento.set(equipamento.getDescricao());
            propriedadeEquipamento.set(getPropriedadeTitle(equipamento.getPropriedade()));

            noteEquipamento.set(note);
        } else {
            prefixoEquipamento.set(mResourceProvider.getString(R.string.no_data_avaliable_initials));
            descricaoEquipamento.set(mResourceProvider.getString(R.string.no_data_avaliable));
            propriedadeEquipamento.set(mResourceProvider.getString(R.string.no_data_avaliable));
            noteEquipamento.set(null);
        }
    }
*//*
    private String getPropriedadeTitle(@PropriedadeEquipamento int propriedade) {
        return propriedade == PropriedadeEquipamento.PROPRIO ? mResourceProvider.getString(R.string.proprio)
                : propriedade == PropriedadeEquipamento.LOCADO ? mResourceProvider.getString(R.string.locado)
                : propriedade == PropriedadeEquipamento.POR_PRODUCAO ? mResourceProvider.getString(R.string.por_producao)
                : "";
    }

    private void setEquipe(Equipe equipe) {
        if (equipe != null) {
            nomeEquipe.set(equipe.getNome());
        } else {
            nomeEquipe.set(mResourceProvider.getString(R.string.no_data_avaliable));
        }
    }

    private void setApontamento(HabitCategoria apontamento) {
        mApontamento = apontamento;

        if (apontamento != null) {
            dataApontamento.set(apontamento.getData());
            turno.set(apontamento.getTurno());
            horimetroInicial.set(apontamento.getHorimetroInicial());
            horimetroFinal.set(apontamento.getHorimetroFinal());
            hodometroInicial.set(apontamento.getHodometroInicial());
            hodometroFinal.set(apontamento.getHodometroFinal());
            horasTrabalhadas.set(apontamento.getHoraTrabalhada());
            quebra.set(apontamento.getQuebra());
            chuva.set(apontamento.getChuva());
            faltaFrente.set(apontamento.getFaltaFrente());
            faltaOperador.set(apontamento.getFaltaOperador());
            manutencao.set(apontamento.getManutencao());
            equipeIncompleta.set(apontamento.getEquipeIncompleta());
            observacao.set(apontamento.getObservacao());
            String exportado = apontamento.getExportado() == null
                    ? "---"
                    : mResourceProvider.getString(R.string.format_datetime, apontamento.getExportado());
            dataExportado.set(exportado);
        } else {
            dataApontamento.set(null);
            turno.set(0);
            horimetroInicial.set(0);
            horimetroFinal.set(0);
            horasTrabalhadas.set(0);
            quebra.set(0);
            chuva.set(0);
            faltaFrente.set(0);
            faltaOperador.set(0);
            manutencao.set(0);
            equipeIncompleta.set(0);
            dataExportado.set(null);
        }
    }
*/
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
