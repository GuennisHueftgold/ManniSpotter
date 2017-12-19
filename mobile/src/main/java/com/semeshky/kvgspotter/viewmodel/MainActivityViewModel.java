package com.semeshky.kvgspotter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivityViewModel extends AndroidViewModel {
    public static final Function<List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>> DISTANCE_STOP_SORT_SHORT_FUNC = new Function<List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>>() {
        @Override
        public List<HomeAdapter.DistanceStop> apply(List<HomeAdapter.DistanceStop> distanceStops) throws Exception {
            Collections.sort(distanceStops, new Comparator<HomeAdapter.DistanceStop>() {
                @Override
                public int compare(HomeAdapter.DistanceStop distanceStop, HomeAdapter.DistanceStop t1) {
                    return Float.compare(distanceStop.distance, t1.distance);
                }
            });
            return distanceStops.subList(0, 5);
        }
    };
    private final BehaviorSubject<Departure> mDepartureBehaviorSubject = BehaviorSubject.create();
    private final Flowable<List<FavoriteStationWithName>> mFavoriteStationLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.mFavoriteStationLiveData = AppDatabase
                .getInstance()
                .favoriteSationDao()
                .getAllWithNameFlow();
    }

    protected void onCleared() {
        super.onCleared();
    }

    public Flowable<List<FavoriteStationWithName>> getFavoriteStations() {
        return this.mFavoriteStationLiveData;
    }

    public Observable<Departure> getDepartureObservable() {
        return this.mDepartureBehaviorSubject;
    }

}
