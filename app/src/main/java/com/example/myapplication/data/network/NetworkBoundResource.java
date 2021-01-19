package com.example.myapplication.data.network;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public abstract class NetworkBoundResource<LocalType, RemoteType> {

    private final FlowableEmitter<Resource<LocalType>> mEmitter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public NetworkBoundResource(FlowableEmitter<Resource<LocalType>> emitter) {
        mEmitter = emitter;

        mEmitter.onNext(Resource.loading(null));

        Disposable disposable = loadFromDb()
                .subscribe(localData -> {
                    if(shouldFetch(localData)){
                        fetchFromNetwork(localData);
                    }else{
                        mEmitter.onNext(Resource.success(localData));
                    }
                }, mEmitter::onError);

        mCompositeDisposable.add(disposable);
    }

    public abstract Completable saveCallResult(@NonNull LocalType data);

    public abstract Single<Resource<RemoteType>> createCall();

    public abstract Flowable<LocalType> loadFromDb();

    public abstract boolean shouldFetch(@Nullable LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

    protected void onFetchFailed(){}    //Hook para liberação ou manipulação de recursos após erro na obtenção de dados

    private void fetchFromNetwork(@NonNull LocalType cachedData){
        mCompositeDisposable.clear();   //Desconecta do fluxo local anterior, já que está desatualizado
        mEmitter.onNext(Resource.loading(cachedData));

        createCall()
                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
                .observeOn(Schedulers.io())     //As vezes é necessário buscar no banco para mapear a entidade
                .subscribe(resource -> {
                    if(resource.status == Status.SUCCESS){
                        saveResultAndReInit(mapper().apply(resource.data));
                    }else{
                        onFetchFailed();
                        mEmitter.onNext(Resource.error(resource.message, cachedData));
                        mEmitter.onComplete();
                    }
                }, throwable -> {
                    Timber.e(throwable, "Erro na busca de dados remotos.");
                    Message message = Message.make("Ocorreu um erro inesperado ao tentar obter dados da rede.");
                    mEmitter.onNext(Resource.error(message, cachedData));
                    mEmitter.onComplete();
                });
    }

    private void saveResultAndReInit(@NonNull LocalType data){
        saveCallResult(data)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> loadFromDb()
                        .map(Resource::success)
                        .subscribe(localTypeResource -> {
                            mEmitter.onNext(localTypeResource);
                            mEmitter.onComplete();
                        }))
                .doOnError(throwable -> {
                    Timber.e(throwable, "Erro ao tentar salvar os dados.");
                    mEmitter.onError(throwable);
                })
                .subscribe();
    }

}
