package com.semeshky.kvgspotter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.github.guennishueftgold.trapezeapi.TrapezeApiService;
import com.github.guennishueftgold.trapezeapi.TripPassages;
import com.github.guennishueftgold.trapezeapi.VehicleLocation;
import com.github.guennishueftgold.trapezeapi.VehicleLocations;
import com.github.guennishueftgold.trapezeapi.VehiclePathInfo;
import com.semeshky.kvgspotter.api.KvgApiClient;

import org.joda.time.DateTime;

import java.lang.annotation.Retention;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class TripPassagesViewModel extends AndroidViewModel {
    public final static int STATUS_REFRESHING = 1, STATUS_FAILED = 2, STATUS_SUCCESS = 4;
    public final ObservableField<String> direction = new ObservableField<>();
    public final ObservableField<String> routeName = new ObservableField<>();
    private MutableLiveData<VehiclePathInfo> mVehiclePathInfoLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mTripIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<TripPassages> mTripPassagesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<VehicleLocation> mVehicleLocationMutableLiveData = new MutableLiveData<>();
    private Call<VehicleLocations> mVehicleLocationCall;
    private Call<TripPassages> mTripPassagesCall;
    private Call<VehiclePathInfo> mVehiclePathInfoCall;
    private MutableLiveData<Integer> mTripPassagesRefreshStatus = new MutableLiveData<>();
    private MutableLiveData<DateTime> mTripPasssagesLastUpdate = new MutableLiveData<>();

    public TripPassagesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<VehiclePathInfo> getVehiclePathInfo() {
        return this.mVehiclePathInfoLiveData;
    }

    public LiveData<String> getTripId() {
        return this.mTripIdMutableLiveData;
    }

    public void setTripId(@NonNull final String tripId) {
        Timber.d("Trip id set: %s", tripId);
        this.mTripIdMutableLiveData.postValue(tripId);
        this.updateData(tripId);
    }

    public LiveData<DateTime> getTripPasssagesLastUpdate() {
        return mTripPasssagesLastUpdate;
    }

    public LiveData<Integer> getTripPassagesRefreshStatus() {
        return this.mTripPassagesRefreshStatus;
    }

    public LiveData<TripPassages> getTripPassages() {
        return this.mTripPassagesMutableLiveData;
    }

    @Override
    protected void onCleared() {
        this.canclePendingRequests();
    }

    private void canclePendingRequests() {
        if (this.mTripPassagesCall != null &&
                !(this.mTripPassagesCall.isCanceled() || this.mTripPassagesCall.isExecuted())) {
            this.mTripPassagesCall.cancel();
        }
        if (this.mVehiclePathInfoCall != null &&
                !(this.mVehiclePathInfoCall.isCanceled() || this.mVehiclePathInfoCall.isExecuted())) {
            this.mVehiclePathInfoCall.cancel();
        }
        if (this.mVehicleLocationCall != null &&
                !(this.mVehicleLocationCall.isCanceled() || this.mVehicleLocationCall.isExecuted())) {
            this.mVehicleLocationCall.cancel();
        }
    }

    private void updateData() {
        this.updateData(this.getTripId().getValue());
    }

    private void updateData(final String tripId) {
        if (tripId == null)
            return;
        this.canclePendingRequests();
        final TrapezeApiService service = KvgApiClient.getService();
        this.mTripPassagesCall = service
                .getTripPassages(tripId,
                        null,
                        "departure");

        this.mVehiclePathInfoCall = service
                .getPathInfoByTripId(tripId);
        this.mVehicleLocationCall = service
                .getVehicleLocations();
        if (this.mVehiclePathInfoLiveData.getValue() == null) {
            this.mVehiclePathInfoCall
                    .enqueue(new Callback<VehiclePathInfo>() {
                        @Override
                        public void onResponse(Call<VehiclePathInfo> call, Response<VehiclePathInfo> response) {
                            TripPassagesViewModel.this.mVehiclePathInfoLiveData.postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<VehiclePathInfo> call, Throwable t) {

                        }
                    });
        }
        this.mTripPassagesRefreshStatus.postValue(STATUS_REFRESHING);
        this.mTripPassagesCall.enqueue(new Callback<TripPassages>() {
            @Override
            public void onResponse(Call<TripPassages> call, Response<TripPassages> response) {
                TripPassagesViewModel.this
                        .mTripPassagesMutableLiveData.postValue(response.body());
                TripPassagesViewModel.this
                        .mTripPassagesRefreshStatus.postValue(STATUS_SUCCESS);
                TripPassagesViewModel.this
                        .mTripPasssagesLastUpdate.postValue(DateTime.now());
            }

            @Override
            public void onFailure(Call<TripPassages> call, Throwable t) {
                Timber.e(t);
                TripPassagesViewModel.this
                        .mTripPassagesRefreshStatus.postValue(STATUS_FAILED);
            }
        });
        this.mVehicleLocationCall
                .enqueue(new Callback<VehicleLocations>() {
                    @Override
                    public void onResponse(Call<VehicleLocations> call, Response<VehicleLocations> response) {
                        for (VehicleLocation vehicleLocation : response.body().getVehicles()) {
                            if (tripId.equalsIgnoreCase(vehicleLocation.getTripId())) {
                                TripPassagesViewModel.this
                                        .mVehicleLocationMutableLiveData.postValue(vehicleLocation);
                                return;
                            }
                        }
                        TripPassagesViewModel.this
                                .mVehicleLocationMutableLiveData.postValue(null);
                    }

                    @Override
                    public void onFailure(Call<VehicleLocations> call, Throwable t) {

                    }
                });
    }

    public LiveData<VehicleLocation> getVehicleLocation() {
        return this.mVehicleLocationMutableLiveData;
    }

    public void refreshData() {
        updateData();
    }

    @Retention(SOURCE)
    @IntDef({STATUS_REFRESHING, STATUS_FAILED, STATUS_SUCCESS})
    public @interface Status {
    }
}
