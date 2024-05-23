package com.example.myapplication.ui.main;

import android.app.Application;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.SingleLiveEvent;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.network.Resource;
import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.utils.DayOfWeek;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {

    private final HabitCategoriRepository mHabitCategoriRepository;

    public final ObservableField<String> busca = new ObservableField<>();

    public final ObservableBoolean dataAvaliable = new ObservableBoolean();

    private final SparseBooleanArray mItemSelectionMap = new SparseBooleanArray();

    private final SingleLiveEvent<Resource<Integer>> mDeleted = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mHabitAdd = new SingleLiveEvent<>();

    private LiveData<Resource<List<HabitCategoriViewItem>>> mItems;

    private DayOfWeek dayOfWeek = DayOfWeek.NONE;

    //public long selecionar=-1;
    public MainViewModel(@NonNull Application application,
                         @NonNull HabitCategoriRepository habitCategoriRepository) {
        super(application);
        mHabitCategoriRepository=habitCategoriRepository;
        load();
    }
    public LiveData<Resource<List<HabitCategoriViewItem>>> getItems() {
        return mItems;
    }

    public void clearItemSelectionMap() {
        mItemSelectionMap.clear();
    }
    public CharSequence[] getDisplayNameArray() {
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
    public List<HabitCategoria> getSelected() {
        List<HabitCategoriViewItem> viewItems = mItems.getValue().data;
        List<HabitCategoria> habitToDelete = new ArrayList<>();

        for (int i = 0; i < mItemSelectionMap.size(); i++) {
            if (mItemSelectionMap.valueAt(i)) {
                int index = mItemSelectionMap.keyAt(i);
                HabitCategoria habitCategoria = viewItems.get(index).getModel();
                habitToDelete.add(habitCategoria);
            }
        }
        return habitToDelete;
    }

    public void deleteSelected() {
        List<HabitCategoria> habitToDelete = getSelected();

        mHabitCategoriRepository.deleteAll(habitToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .toSingle(() -> Resource.success(habitToDelete.size()))
                .doOnError(throwable -> Timber.e(throwable, "Erro ao tentar deletar os Hábitos."))
                .onErrorReturn(Resource::error)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDeleted::setValue);
    }

    private void load() {
        mItems = LiveDataReactiveStreams.fromPublisher(getPublisher()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()));
    }
    private Flowable<Resource<List<HabitCategoriViewItem>>> getPublisher() {
        return Flowable.create(e -> {
            e.onNext(Resource.loading(null));

            mHabitCategoriRepository.list(dayOfWeek)
                    .observeOn(Schedulers.computation())
                    .map(this::sortAndMapToFlexibleItem)
                    .map(Resource::success)
                    .onErrorReturn(Resource::error)
                    .subscribe(e::onNext);
        }, BackpressureStrategy.BUFFER);
    }
    private List<HabitCategoriViewItem> sortAndMapToFlexibleItem(List<? extends HabitCategoria> habitCategorias){
        List<? extends HabitCategoria> sortedList = new ArrayList<>(habitCategorias);
        Collections.sort(sortedList, this::sortByCodigoAsc);
        return Lists.transform(sortedList, HabitCategoriViewItem::new);
    }

    private int sortByCodigoAsc(@NonNull HabitCategoria habitCategoria1, @NonNull HabitCategoria habitCategoria2) {
        return Long.compare(habitCategoria1.getId(), habitCategoria2.getId());
    }

    public SingleLiveEvent<Void> getHabitAdd() {
        return mHabitAdd;
    }

    public void openAddHabit() {
        mHabitAdd.call();
    }

    public void setDay(DayOfWeek day){
        this.dayOfWeek = day;
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
