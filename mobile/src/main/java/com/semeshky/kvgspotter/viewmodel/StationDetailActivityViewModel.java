package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.Station;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStation;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.database.StopPoint;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.Response;
import timber.log.Timber;

public final class StationDetailActivityViewModel extends ViewModel {
    public final ObservableField<String> stationName = new ObservableField<>();
    public final ObservableField<String> stationShortName = new ObservableField<>();
    public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    public final ObservableField<String> lastUpdateTimestamp = new ObservableField<>("");
    private final MutableLiveData<Station> mStationMutableLiveData = new MutableLiveData<>();
    private final AtomicBoolean mIsRefreshing = new AtomicBoolean(false);
    private final MediatorLiveData<Boolean> mFavoritedLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<StopPoint>> mStopPointsLiveData = new MediatorLiveData<>();
    private LiveData<List<StopPoint>> mStopPointsDatabaseLiveData;
    private LiveData<Boolean> mFavoriteDatabaseLiveData;
    private LiveData<Stop> mStopDatabaseLiveData;
    private MediatorLiveData<Stop> mStopLiveData = new MediatorLiveData<>();
    private DisposableSubscriber<Station> mStationSubscription;

    public LiveData<Boolean> isStationFavorited() {
        return this.mFavoritedLiveData;
    }

    public LiveData<Station> getStation() {
        return this.mStationMutableLiveData;
    }

    public void setStation(final Station station) {
        this.isRefreshing.set(false);
        if (station != null) {
            this.mFavoritedLiveData.setValue(false);
            this.lastUpdateTimestamp.set(LocalTime.now().toString(DateTimeFormat.shortTime()));
            this.stationShortName.set(station.getStopShortName());
            this.stationName.set(station.getStopName());
            if (this.mFavoriteDatabaseLiveData != null) {
                this.mFavoritedLiveData.removeSource(this.mFavoriteDatabaseLiveData);
            }
            this.mFavoriteDatabaseLiveData = AppDatabase.getInstance()
                    .favoriteSationDao()
                    .isFavoriteStationSync(station.getStopShortName());
            this.mFavoritedLiveData.addSource(this.mFavoriteDatabaseLiveData, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    mFavoritedLiveData.setValue(aBoolean);
                }
            });
            // RENEW STOP POINTS LISTENER
            if (this.mStopPointsDatabaseLiveData != null) {
                this.mStopPointsLiveData.removeSource(this.mStopPointsDatabaseLiveData);
            }
            this.mStopPointsDatabaseLiveData = AppDatabase.getInstance()
                    .stopPointDao()
                    .getAllWithShortName(stationShortName.get());
            this.mStopPointsLiveData.addSource(this.mStopPointsDatabaseLiveData, new Observer<List<StopPoint>>() {
                @Override
                public void onChanged(@Nullable List<StopPoint> stopPoints) {
                    mStopPointsLiveData.setValue(stopPoints);
                }
            });
            // RENEW STOP LISTENER
            if (this.mStopDatabaseLiveData != null) {
                this.mStopLiveData.removeSource(this.mStopDatabaseLiveData);
            }
            this.mStopDatabaseLiveData = AppDatabase.getInstance()
                    .stopDao()
                    .getStopByShortNameSync(stationShortName.get());
            this.mStopLiveData.addSource(this.mStopDatabaseLiveData, new Observer<Stop>() {
                @Override
                public void onChanged(@Nullable Stop stop) {
                    mStopLiveData.setValue(stop);
                }
            });
        }
        this.mStationMutableLiveData.postValue(station);
    }

    public Single<Boolean> favoriteStation() {
        return this.favoriteStation(this.stationShortName.get());
    }

    public Single<Boolean> favoriteStation(final String shortName) {
        if (shortName == null) {
            return Single.just(false);
        }
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                final FavoriteStation favoriteStation = new FavoriteStation();
                favoriteStation.setShortName(shortName);
                final long result = AppDatabase
                        .getInstance()
                        .favoriteSationDao()
                        .insert(favoriteStation);
                emitter.onSuccess(result >= 0);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> toggleFavorite() {
        if (this.mFavoritedLiveData.getValue()) {
            Timber.d("Remove favorite");
            return this.removeFavorite(this.stationShortName.get())
                    .map(new Function<Boolean, Boolean>() {
                        @Override
                        public Boolean apply(Boolean aBoolean) throws Exception {
                            return !aBoolean;
                        }
                    });
        } else {
            Timber.d("Give favorite");
            return this.favoriteStation(this.stationShortName.get());
        }
    }

    public Single<Boolean> removeFavorite(final String shortName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                final int result = AppDatabase
                        .getInstance()
                        .favoriteSationDao()
                        .delete(shortName);
                Timber.d("Remove result: " + result);
                emitter.onSuccess(result == 1);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void startSyncService() {
        if (this.mStationSubscription != null)
            return;
        this.mStationSubscription = new DisposableSubscriber<Station>() {
            @Override
            public void onNext(Station station) {
                StationDetailActivityViewModel
                        .this
                        .setStation(station);
            }

            @Override
            public void onError(Throwable t) {
                Timber.e(t);
                StationDetailActivityViewModel
                        .this
                        .setStationLoadError(t);
            }

            @Override
            public void onComplete() {
                Timber.d("Station subscription completed");
            }
        };
        getFlowSation().subscribe(this.mStationSubscription);
    }

    public void stopSyncService() {
        if (this.mStationSubscription != null) {
            this.mStationSubscription.dispose();
            this.mStationSubscription = null;
        }
    }

    private Station loadStation() throws IOException {
        if (stationShortName.get() == null) {
            return null;
        }
        Response<Station> resp = KvgApiClient.getInstance()
                .getService()
                .getStation(stationShortName.get(), "departure")
                .execute();
        if (resp.code() == 200) {
            return resp.body();
        } else {
            throw new RuntimeException("Error querying source information");
        }
    }

    private Flowable<Station> getFlowSation() {
        return Flowable.interval(0, 20, TimeUnit.SECONDS)
                .map(new Function<Long, Station>() {
                    @Override
                    public Station apply(Long aLong) throws Exception {
                        StationDetailActivityViewModel
                                .this
                                .isRefreshing.set(false);
                        return loadStation();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<List<StopPoint>> getStopPoints() {
        return this.mStopPointsLiveData;
    }

    public LiveData<Stop> getStop() {
        return this.mStopLiveData;
    }

    public void refresh() {
        this.isRefreshing.set(true);
        Single.create(new SingleOnSubscribe<Station>() {
            @Override
            public void subscribe(SingleEmitter<Station> emitter) throws Exception {
                try {
                    emitter.onSuccess(loadStation());
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Station>() {
                    @Override
                    public void onSuccess(Station station) {
                        StationDetailActivityViewModel
                                .this
                                .setStation(station);
                    }

                    @Override
                    public void onError(Throwable e) {
                        StationDetailActivityViewModel
                                .this
                                .setStationLoadError(e);
                        Timber.e(e);
                    }
                });
    }

    public void setStationLoadError(Throwable stationLoadError) {

    }
}
