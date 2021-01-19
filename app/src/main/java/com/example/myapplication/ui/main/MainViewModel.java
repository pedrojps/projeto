package com.example.myapplication.ui.main;

import android.app.Application;
import android.util.SparseBooleanArray;
import android.widget.HeaderViewListAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.network.Resource;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewHolder;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {

    private final HabitCategoriRepository mHabitCategoriRepository;

    public final ObservableBoolean dataAvaliable = new ObservableBoolean();

    private final SparseBooleanArray mItemSelectionMap = new SparseBooleanArray();

    private final SingleLiveEvent<Resource<Integer>> mProjetosDeleted = new SingleLiveEvent<>();

    private LiveData<Resource<List<HabitCategoriViewItem>>> mItems;

    public MainViewModel(@NonNull Application application,
                         @NonNull HabitCategoriRepository habitCategoriRepository) {
        super(application);
        mHabitCategoriRepository=habitCategoriRepository;
        loadProjetos();
    }
    public LiveData<Resource<List<HabitCategoriViewItem>>> getItems() {
        return mItems;
    }

    public void clearItemSelectionMap() {
        mItemSelectionMap.clear();
    }
    public CharSequence[] getProjetoDisplayNameArray() {
        List<HabitCategoriViewItem> viewItems = mItems.getValue().data;
        CharSequence[] displayNames = new CharSequence[viewItems.size()];

        int i = 0;
        for (HabitCategoriViewItem viewItem : viewItems) {
            displayNames[i] = viewItem.getModel().getNome();
            i++;
        }
        return displayNames;
    }

    public void toggleItemSelection(int position, boolean isSelected) {
        mItemSelectionMap.put(position, isSelected);
    }
    public List<HabitCategoria> getProjetosSelected() {
        List<HabitCategoriViewItem> viewItems = mItems.getValue().data;
        List<HabitCategoria> projetosToDelete = new ArrayList<>();

        for (int i = 0; i < mItemSelectionMap.size(); i++) {
            if (mItemSelectionMap.valueAt(i)) {
                int index = mItemSelectionMap.keyAt(i);
                HabitCategoria projeto = viewItems.get(index).getModel();
                projetosToDelete.add(projeto);
            }
        }
        return projetosToDelete;
    }

    public void deleteProjetosSelected() {
        List<HabitCategoria> projetosToDelete = getProjetosSelected();

        mHabitCategoriRepository.deleteAll(projetosToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .toSingle(() -> Resource.success(projetosToDelete.size()))
                .doOnError(throwable -> Timber.e(throwable, "Erro ao tentar deletar os Habitos."))
                .onErrorReturn(Resource::error)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mProjetosDeleted::setValue);
    }
    /////
    public void teste (){
        mHabitCategoriRepository.insert(new HabitCategoria("teste 2",new LocalDate(new Date("12/12/2000")),"teste 1"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void loadProjetos() {
        mItems = LiveDataReactiveStreams.fromPublisher(getProjetoPublisher()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()));
    }
    private Flowable<Resource<List<HabitCategoriViewItem>>> getProjetoPublisher() {
        return Flowable.create(e -> {
            e.onNext(Resource.loading(null));

            mHabitCategoriRepository.list()
                    .observeOn(Schedulers.computation())
                    .map(this::sortAndMapToFlexibleItem)
                    .map(Resource::success)
                    .onErrorReturn(Resource::error)
                    .subscribe(e::onNext);
        }, BackpressureStrategy.BUFFER);
    }
    private List<HabitCategoriViewItem> sortAndMapToFlexibleItem(List<HabitCategoria> projetos){
        List<HabitCategoria> sortedList = new ArrayList<>(projetos);
        Collections.sort(sortedList, this::sortByCodigoProjetoAsc);
        return Lists.transform(sortedList, HabitCategoriViewItem::new);
    }

    private int sortByCodigoProjetoAsc(@NonNull HabitCategoria projeto1, @NonNull HabitCategoria projeto2) {
        return Long.compare(projeto1.getId(), projeto2.getId());
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;

        private final HabitCategoriRepository mHabitCategoriRepository;

        public Factory(
                @NonNull Application application,
                @NonNull HabitCategoriRepository habitCategoriRepository
        ) {
            mApplication = application;
            mHabitCategoriRepository = habitCategoriRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainViewModel(mApplication, mHabitCategoriRepository);
        }
    }
}
