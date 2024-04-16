package com.example.myapplication.ui.graficos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.common.lifecycle.ErrorDialogMessage;
import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalDateFormat;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.types.TipoVariavel;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.source.local.projection.HabitEntyDetails;
import com.example.myapplication.data.source.local.projection.ItensEntyProject;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GraficViewModel extends AndroidViewModel {

    private HabitCategoria mHabitCategoria;

    private final ErrorDialogMessage mErrorDialogMessage = new ErrorDialogMessage();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private final HabitCategoriRepository mCategoriRepository;

    private final SingleLiveEvent<Void> mChangeStartDate = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mChangeEndDate = new SingleLiveEvent<>();

    public final ObservableField<LocalDate> startDate = new ObservableField<>(new LocalDate());

    public final ObservableField<LocalDate> endDate = new ObservableField<>(new LocalDate());

    private List<HabitEntyDetails> mListVariaveis =new ArrayList<>();

    private final SingleLiveEvent<Void> mCarregaEnty = new SingleLiveEvent<>();
    public GraficViewModel(
            @NonNull Application application,
            @NonNull HabitCategoriRepository habitCategoriRepository,
            HabitCategoria habitCategoria
    ) {
        super(application);
        mCategoriRepository = habitCategoriRepository;
        mHabitCategoria = habitCategoria;
        initDates();
        loadEntyStart();
    }

    private void loadEntyStart(){

        mCategoriRepository.findByIdAndDataPeriodDetails(mHabitCategoria.getId(),startDate.get(),endDate.get())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::addDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apontWithDetails -> {
                    mListVariaveis = apontWithDetails;
                    mCarregaEnty.call();
                }, this::showError);

    }

    public  SingleLiveEvent<Void> getCarregaEnty(){
        return mCarregaEnty;
    }

    public int getSizeList(){
        return mListVariaveis.size();
    }

    public String aditionalText(){
        String text = "";
        HashMap<ItemCategoria, Integer> somatoria = new HashMap<>();

        for(HabitEntyDetails enty : mListVariaveis) {
            for(ItensEntyProject item : enty.itensEntyProjects) {
                ItemCategoria itemC = item.itemCategorias.get(0);

                if(itemC.getTipo() == TipoVariavel.VALOR.value){
                    int valor;
                    try {
                        valor =  somatoria.get(itemC);
                    }
                    catch (Exception e) {
                        valor = 0;
                    }

                    int foo;
                    try {
                        foo = Integer.parseInt(item.itemEnty.getValor());
                    }
                    catch (Exception e) {
                        foo = 0;
                    }
                    valor = valor + foo;

                    somatoria.put(itemC,valor);
                }
            }
        }

        for (ItemCategoria itemc : somatoria.keySet()){
            String t =  getApplication().getString(R.string.media_diaria_variavel, itemc.getNome(), cacularByDay(somatoria.get(itemc))+"");
            text = text + t;

        }

        return text;
    }

    List<Entry> setData() {
        ArrayList<Entry> entries = new ArrayList<>();


        HashMap<String, Integer> byDay = new HashMap<>();
        for(HabitEntyDetails enty : mListVariaveis) {
            String day = enty.habitEnty.getData().toString(LocalDateFormat.DATE2);
            int cont;
            try {
                cont = byDay.get(day);
            }
            catch (Exception e) {
                cont = 0;
            }

            int foo = cont + 1;

            byDay.put(day,foo);
        }

        LocalDateTime lda = convertToLocalDateViaInstant(startDate.get());
        LocalDateTime ldb = convertToLocalDateViaInstant(endDate.get());

        List<String> dates = new ArrayList<>();
        for (LocalDateTime ld = lda; !ld.isAfter(ldb); ld = ld.plusDays(1)) {
            dates.add(
                    ld.format(DateTimeFormatter.ofPattern(LocalDateFormat.DATE2))
            );
        }

        List<Entry> valsComp1 = new ArrayList<Entry>();

        for (String date : dates){
            int cont = 0;
            try {
                cont = byDay.get(date);
            }
            catch (Exception e) {
                cont = 0;
            }
            valsComp1.add(new Entry(dates.indexOf(date), cont));
        }

        return valsComp1;

    }

    public LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    private float cacularByDay(float valor){

        Calendar data1 = Calendar.getInstance();
        Calendar data2 = Calendar.getInstance();
        try {
            data1.setTime(startDate.get());
            data2.setTime(endDate.get());
        } catch (Exception e ) {}
        int dias = data2.get(Calendar.DAY_OF_YEAR) -
                data1.get(Calendar.DAY_OF_YEAR);

        if (dias== 0 )
            dias = 1;

        return valor/dias;
    }

    protected void initDates() {
        Calendar calendar = Calendar.getInstance();
        endDate.set(new LocalDate(calendar.getTime()));

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) - 1));
        startDate.set(new LocalDate(calendar.getTime()));
    }

    private void showError(Throwable throwable) {
        mErrorDialogMessage.setValue("Erro", throwable.getMessage());
    }
    public String getHabitName(){
        return mHabitCategoria.getNome();
    }

    public void showStartData(){
        mChangeStartDate.call();
    }

    public void showEndData(){
        mChangeEndDate.call();
    }

    private void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public SingleLiveEvent<Void> getChangeStartDate() {
        return mChangeStartDate;
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
    }

    public void setStartDate(Date newDate){
        startDate.set(new LocalDate(newDate));
        loadEntyStart();
    }

    public void setEndDate(Date newDate){
        endDate.set(new LocalDate(newDate));
        loadEntyStart();
    }

    public SingleLiveEvent<Void> getChangeEndDate() {
        return mChangeEndDate;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;

        private final HabitCategoriRepository mApontEquipamentoRepository;

        private final HabitCategoria mHabitCategoria;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository apontEquipamentoRepository,
                HabitCategoria habitCategoria
        ) {
            mApplication = application;
            mApontEquipamentoRepository = apontEquipamentoRepository;
            mHabitCategoria = habitCategoria;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new GraficViewModel(mApplication, mApontEquipamentoRepository, mHabitCategoria);
        }
    }
}
