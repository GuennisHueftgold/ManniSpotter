package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.IntDef;

import com.github.guennishueftgold.trapezeapi.Station;
import com.github.guennishueftgold.trapezeapi.TripPassages;
import com.github.guennishueftgold.trapezeapi.VehicleLocation;
import com.github.guennishueftgold.trapezeapi.VehicleLocations;
import com.semeshky.kvgspotter.api.KvgApiClient;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.Stop;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ActivityLiveMapViewModel extends ViewModel {
    public final static int DETAILS_STATUS_CLOSED = 1,
            DETAILS_STATUS_SHOW_STOP = 2,
            DETAILS_STATUS_SHOW_TRIP = 3;
    private final MutableLiveData<Integer> mDetailsStatusLiveData = new MutableLiveData<>();
    protected LiveData<List<Stop>> mStopsLiveData;
    private MutableLiveData<VehicleLocations> mVehicleLocationsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Stop> mStopSelectedLiveData = new MutableLiveData<>();
    private MutableLiveData<VehicleLocation> mVehicleSelectedLiveData = new MutableLiveData<>();
    private DisposableSubscriber<VehicleLocations> mVehicleLocationUpdater;

    public ActivityLiveMapViewModel() {
        this.mDetailsStatusLiveData.setValue(DETAILS_STATUS_CLOSED);
        this.mStopsLiveData = AppDatabase
                .getInstance()
                .stopDao()
                .getAllSync();
    }

    public LiveData<Integer> getDetailsStatus() {
        return this.mDetailsStatusLiveData;
    }

    public void setDetailsStatus(@DETAILS_STATUS int detailsStatus) {
        this.mDetailsStatusLiveData.postValue(detailsStatus);
    }

    public LiveData<VehicleLocations> getVehicleLocations() {
        return this.mVehicleLocationsMutableLiveData;
    }

    public LiveData<Stop> getSelectedStop() {
        return this.mStopSelectedLiveData;
    }

    public void setSelectedStop(Stop stop) {
        this.mStopSelectedLiveData.postValue(stop);
        this.mDetailsStatusLiveData.postValue(DETAILS_STATUS_SHOW_STOP);
    }

    public LiveData<List<Stop>> getStops() {
        return this.mStopsLiveData;
    }

    public LiveData<VehicleLocation> getSelectedVehicle() {
        return this.mVehicleSelectedLiveData;
    }

    public void setSelectedVehicle(VehicleLocation vehicleLocation) {
        this.mVehicleSelectedLiveData.postValue(vehicleLocation);
        this.mDetailsStatusLiveData.postValue(DETAILS_STATUS_SHOW_TRIP);
    }

    private VehicleLocations loadVehicleLocations() throws IOException {
        Response<VehicleLocations> resp = KvgApiClient.getService()
                .getVehicleLocations()
                .execute();
        if (resp.code() == 200) {
            return resp.body();
        } else {
            throw new RuntimeException("Error querying source information");
        }
    }

    private Flowable<VehicleLocations> createVehicleUpdater() {
        return Flowable.interval(0, 20, TimeUnit.SECONDS)
                .map(new Function<Long, VehicleLocations>() {
                    @Override
                    public VehicleLocations apply(Long aLong) throws Exception {
                        return loadVehicleLocations();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void startVehicleLocationUpdater() {
        if (this.mVehicleLocationUpdater != null)
            return;
        this.mVehicleLocationUpdater = new DisposableSubscriber<VehicleLocations>() {
            @Override
            public void onNext(VehicleLocations vehicleLocations) {
                ActivityLiveMapViewModel.this
                        .mVehicleLocationsMutableLiveData.postValue(vehicleLocations);
            }

            @Override
            public void onError(Throwable t) {
                Timber.e(t);
            }

            @Override
            public void onComplete() {
                Timber.d("Station subscription completed");
                ActivityLiveMapViewModel
                        .this
                        .mVehicleLocationUpdater = null;
            }
        };
        createVehicleUpdater().subscribe(this.mVehicleLocationUpdater);
    }

    public void stopVehicleLocationUpdater() {
        if (this.mVehicleLocationUpdater != null) {
            this.mVehicleLocationUpdater.dispose();
            this.mVehicleLocationUpdater = null;
        }
    }

    public void refreshData() {
        KvgApiClient
                .getService()
                .getVehicleLocations()
                .enqueue(new Callback<VehicleLocations>() {
                    @Override
                    public void onResponse(Call<VehicleLocations> call, Response<VehicleLocations> response) {
                        setBusses(response.body());
                    }

                    @Override
                    public void onFailure(Call<VehicleLocations> call, Throwable t) {

                    }
                });
    }

    private void setBusses(VehicleLocations vehicleLocations) {
        this.mVehicleLocationsMutableLiveData.postValue(vehicleLocations);
    }

    public Single<Station> loadVehicleLocations(final String shortName) {
        return Single.create(new SingleOnSubscribe<Station>() {
            @Override
            public void subscribe(SingleEmitter<Station> e) throws Exception {
                if (shortName == null) {
                    e.onError(new RuntimeException("No name provided"));
                }
                Response<Station> resp = KvgApiClient.getService()
                        .getStation(shortName, "departure")
                        .execute();
                if (resp.code() == 200) {
                    e.onSuccess(resp.body());
                } else {
                    e.onError(new RuntimeException("Error querying source information"));
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<TripPassages> loadTripPassages(final String tripId) {
        return Single.create(new SingleOnSubscribe<TripPassages>() {
            @Override
            public void subscribe(SingleEmitter<TripPassages> e) throws Exception {
                if (tripId == null) {
                    e.onError(new RuntimeException("No name provided"));
                }
                Response<TripPassages> resp = KvgApiClient.getService()
                        .getTripPassages(tripId, "departure")
                        .execute();
                if (resp.code() == 200) {
                    e.onSuccess(resp.body());
                } else {
                    e.onError(new RuntimeException("Error querying source information"));
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Retention(SOURCE)
    @IntDef({DETAILS_STATUS_CLOSED, DETAILS_STATUS_SHOW_STOP, DETAILS_STATUS_SHOW_TRIP})
    public @interface DETAILS_STATUS {
    }
}
