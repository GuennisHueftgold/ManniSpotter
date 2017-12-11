package com.semeshky.kvgspotter.activities;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.VehicleLocations;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.ActivityLiveMapBinding;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveMapActivity extends AppCompatActivity {

    protected ActivityLiveMapBinding mBinding;
    protected ActivityLiveMapViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_live_map);
        this.mViewModel = ViewModelProviders.of(this).get(ActivityLiveMapViewModel.class);
        this.setSupportActionBar(this.mBinding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.live_map, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        this.mViewModel.refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                this.mViewModel.refreshData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
