package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvg.kvgapi.VehicleLocations;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.ActivityLiveMapBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLiveMapViewModel extends ViewModel{
    protected LiveData<List<Stop>> mStopsLiveData;
    private MutableLiveData<VehicleLocations> mVehicleLocationsMutableLiveData=new MutableLiveData<>();

    public ActivityLiveMapViewModel(){
        this.mStopsLiveData = AppDatabase
                .getInstance()
                .stopDao()
                .getAllSync();
    }

    public LiveData<VehicleLocations> getVehicleLocations(){
        return this.mVehicleLocationsMutableLiveData;
    }

    public LiveData<List<Stop>> getStops(){
        return this.mStopsLiveData;
    }

    public void refreshData() {
        KvgApiClient.getInstance()
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

}
