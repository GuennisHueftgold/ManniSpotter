package com.semeshky.kvgspotter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivityViewModel extends AndroidViewModel {
    private final BehaviorSubject<Departure> mDepartureBehaviorSubject = BehaviorSubject.create();
    private final LiveData<List<FavoriteStationWithName>> mFavoriteStationLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.mFavoriteStationLiveData = AppDatabase.getInstance()
                .favoriteSationDao()
                .getAllWithNameSync();
    }

    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<FavoriteStationWithName>> getFavoriteStations() {
        return this.mFavoriteStationLiveData;
    }

    public Observable<Departure> getDepartureObservable() {
        return this.mDepartureBehaviorSubject;
    }
}
