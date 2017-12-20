package com.semeshky.kvgspotter.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.databinding.ActivityMainBinding;
import com.semeshky.kvgspotter.fragments.RequestLocationPermissionDialogFragment;
import com.semeshky.kvgspotter.presenter.MainActivityPresenter;
import com.semeshky.kvgspotter.rx.LocationFlowableOnSubscribe;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCESS_LOCATION = 2928;
    private final HomeAdapter.HomeAdapterEventListener mFavoriteSelectedListener = new HomeAdapter.HomeAdapterEventListener() {
        @Override
        public void onFavoriteSelected(@NonNull String shortName, @Nullable String name) {
            final Intent intent = StationDetailActivity.createIntent(MainActivity.this,
                    shortName,
                    name);
            startActivity(intent);
        }

        @Override
        public void onRequestPermission() {
            MainActivity
                    .this
                    .requestLocationPermission();
        }
    };
    private ActivityMainBinding mBinding;
    private MainActivityPresenter mMainActivityPresenter;
    private MainActivityViewModel mViewModel;
    private HomeAdapter mHomeAdapter;
    private FusedLocationProviderClient mFusedLocationProvider;
    private Subscription mStopSubscription;
    private Disposable mFavoriteDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.mViewModel = ViewModelProviders.of(this)
                .get(MainActivityViewModel.class);
        this.setSupportActionBar((Toolbar) this.mBinding.toolbar.getRoot());
        this.mMainActivityPresenter = new MainActivityPresenter(this);
        this.mHomeAdapter = new HomeAdapter(this.mFavoriteSelectedListener);
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.recyclerView.setAdapter(this.mHomeAdapter);
        this.mBinding.setPresenter(this.mMainActivityPresenter);
        this.mBinding.executePendingBindings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, StationSearchActivity.class)));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_live_map:
                startActivity(new Intent(this, LiveMapActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Timber.d("approved perms");
                } else {
                    Timber.d("Location permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    private void requestLocationPermission() {
        this.requestLocationPermission(true);
    }

    private void requestLocationPermission(boolean showDialog) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && showDialog) {
                showRequestPermissionDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        Flowable<Location> locationFlowable = null;
        if (this.hasLocationPermission()) {
            this.mHomeAdapter.setHasLocationPermission(true);
            this.mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);

            LocationFlowableOnSubscribe locationFlowableOnSubscribe = new LocationFlowableOnSubscribe();
            final LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10000)
                    .setFastestInterval(5000)
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            this.mFusedLocationProvider.requestLocationUpdates(locationRequest,
                    locationFlowableOnSubscribe, null);
            this.mFusedLocationProvider.getLastLocation()
                    .addOnSuccessListener(locationFlowableOnSubscribe.getOnSuccessListener());
            locationFlowable =
                    Flowable.create(locationFlowableOnSubscribe, BackpressureStrategy.LATEST);
            this.mViewModel.createNearbyFlowable(locationFlowable)
                    .subscribe(new Consumer<List<HomeAdapter.DistanceStop>>() {
                        @Override
                        public void accept(List<HomeAdapter.DistanceStop> distanceStops) throws Exception {
                            Timber.d("Stops set");
                            mHomeAdapter.setNearby(distanceStops);
                        }
                    });

        } else {
            this.mHomeAdapter.setHasLocationPermission(false);
        }
        this.mFavoriteDisposable = this.mViewModel
                .getFavoriteFlowable(locationFlowable)
                .subscribe(new Consumer<List<HomeAdapter.DistanceStop>>() {
                    @Override
                    public void accept(List<HomeAdapter.DistanceStop> distanceStops) throws Exception {
                        Timber.d("Stops set");
                        if (distanceStops == null || distanceStops.size() == 0) {
                            MainActivity
                                    .this
                                    .mMainActivityPresenter
                                    .listContainsItems.set(false);
                        } else {
                            MainActivity
                                    .this
                                    .mMainActivityPresenter
                                    .listContainsItems.set(true);
                        }
                        mHomeAdapter.setFavorites(distanceStops);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mFusedLocationProvider != null) {
            Timber.d("Removed update listener");
        }
        this.mFavoriteDisposable.dispose();
        if (this.mStopSubscription != null) {
            this.mStopSubscription.cancel();
            this.mStopSubscription = null;
        }
    }

    private void showRequestPermissionDialog() {
        RequestLocationPermissionDialogFragment dialog = new RequestLocationPermissionDialogFragment();
        dialog.setOnLocationRequestDialogListener(new RequestLocationPermissionDialogFragment.OnLocationRequestDialogListener() {
            @Override
            public void onApproveRequest(boolean approved) {
                if (!approved)
                    return;
                MainActivity
                        .this
                        .requestLocationPermission(false);
            }
        });
        dialog.show(this.getSupportFragmentManager(), "ask_for_location");
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
