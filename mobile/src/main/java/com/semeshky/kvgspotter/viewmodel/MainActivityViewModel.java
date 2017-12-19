package com.semeshky.kvgspotter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivityViewModel extends AndroidViewModel {
    private final BehaviorSubject<Departure> mDepartureBehaviorSubject = BehaviorSubject.create();
    private final LiveData<List<FavoriteStationWithName>> mFavoriteStationLiveData;
    private final MutableLiveData<Location> mLocationLiveData = new MutableLiveData<>();
    public final LocationCallback LocationUpdateCallback = new LocationCallback() {

        public void onLocationResult(LocationResult var1) {
            MainActivityViewModel.this
                    .mLocationLiveData.postValue(var1.getLastLocation());
        }
    };

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

    public LiveData<Location> getLocation() {
        return mLocationLiveData;
    }

    public void setLocation(Task<Location> location) {
        location.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                MainActivityViewModel
                        .this
                        .mLocationLiveData
                        .postValue(location);
            }
        });
    }

}
