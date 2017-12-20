package com.semeshky.kvgspotter.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvg.kvgapi.TripPassages;
import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.TripPassagesAdapter;
import com.semeshky.kvgspotter.databinding.ActivityTripPassagesBinding;
import com.semeshky.kvgspotter.fragments.TripPassagesFragment;
import com.semeshky.kvgspotter.fragments.VehicleRouteFragment;
import com.semeshky.kvgspotter.viewmodel.TripPassagesViewModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.concurrent.atomic.AtomicBoolean;

public final class TripPassagesActivity extends AppCompatActivity {
    private final static String EXTRA_TRIP_ID = "extra_trip_id";
    private final static String EXTRA_VEHICLE_ID = "extra_vehicle_id";
    private final static String EXTRA_MODE = "extra_mode";
    private final static String EXTRA_DIRECTION = "extra_direction";
    private final static String EXTRA_ROUTE_NAME = "extra_route_name";
    private final AtomicBoolean mIsRefreshing = new AtomicBoolean(false);
    private ActivityTripPassagesBinding mBinding;
    private final Observer<TripPassages> mTripPassagesObserver = new Observer<TripPassages>() {
        @Override
        public void onChanged(@Nullable TripPassages tripPassages) {
            if (tripPassages != null) {
                TripPassagesActivity.this.mBinding.setToolbarTitle(tripPassages.getRouteName() + " - " + tripPassages.getDirectionText());
                TripPassagesActivity.this.mBinding.executePendingBindings();
            }
        }
    };
    private TripPassagesAdapter mDepartureAdapter;
    private MutableLiveData<TripPassages> mTripPassagesMutableLiveData = new MutableLiveData<>();
    private String mVehicleId;
    private String mTripId;
    private String mMode;
    private TripPassagesViewModel mViewModel;
    private Observer<Integer> mTripPassagesStatusObserver = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer status) {
            if (status == TripPassagesViewModel.STATUS_FAILED) {
                Snackbar snackbar = Snackbar
                        .make(TripPassagesActivity.this
                                .mBinding.coordinatorLayout, R.string.refreshing_data_failed, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };
    private Observer<DateTime> mTripPassagesLastUpdateObserver = new Observer<DateTime>() {
        @Override
        public void onChanged(@Nullable DateTime dateTime) {
            if (dateTime == null) {
                TripPassagesActivity.this
                        .mBinding.setLastUpdate(null);
            } else {
                TripPassagesActivity.this
                        .mBinding.setLastUpdate(dateTime.toString(DateTimeFormat.shortTime()));
            }
            TripPassagesActivity.this
                    .mBinding.executePendingBindings();
        }
    };

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull final Departure departure) {
        return createIntent(context,
                departure.getTripId(),
                departure.getDirection(),
                departure.getPatternText(),
                departure.getVehicleId(),
                "departure");
    }

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull final String tripId,
                                      @Nullable final String direction,
                                      @Nullable final String routeName,
                                      @Nullable final String vehicleId,
                                      @NonNull final String mode) {
        final Intent intent = new Intent(context, TripPassagesActivity.class);
        intent.putExtra(EXTRA_TRIP_ID, tripId);
        intent.putExtra(EXTRA_VEHICLE_ID, vehicleId);
        intent.putExtra(EXTRA_MODE, mode);
        intent.putExtra(EXTRA_ROUTE_NAME, routeName);
        intent.putExtra(EXTRA_DIRECTION, direction);
        return intent;
    }

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull VehicleLocation vehicleLocation) {
        return TripPassagesActivity
                .createIntent(context,
                        vehicleLocation.getTripId(),
                        Long.toString(vehicleLocation.getId()),
                        "departure",
                        vehicleLocation.getName(),
                        null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(this).get(TripPassagesViewModel.class);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trip_passages);
        this.mViewModel.setTripId(this.getIntent().getExtras().getString(EXTRA_TRIP_ID));
        this.mViewModel.routeName.set(this.getIntent().getExtras().getString(EXTRA_ROUTE_NAME));
        this.mViewModel.direction.set(this.getIntent().getExtras().getString(EXTRA_DIRECTION));
        this.mVehicleId = this.getIntent().getExtras().getString(EXTRA_VEHICLE_ID, null);
        this.mMode = this.getIntent().getExtras().getString(EXTRA_MODE);
        this.setSupportActionBar(this.mBinding.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.mBinding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        this.mBinding.tabLayout.setupWithViewPager(this.mBinding.viewPager);
        this.mViewModel.getTripPassages()
                .observe(this, this.mTripPassagesObserver);
        this.mViewModel.getTripPassagesRefreshStatus()
                .observe(this, this.mTripPassagesStatusObserver);
        this.mViewModel.getTripPasssagesLastUpdate()
                .observe(this, this.mTripPassagesLastUpdateObserver);
        final String routeName = this.mViewModel.routeName.get();
        final String routeDirection = this.mViewModel.direction.get();
        if (routeName != null && routeDirection != null) {
            this.mBinding.setToolbarTitle(routeName + " - " + routeDirection);
        } else if (routeName == null) {
            this.mBinding.setToolbarTitle(routeDirection);
        } else if (routeDirection == null) {
            this.mBinding.setToolbarTitle(routeName);
        } else {
            this.mBinding.setToolbarTitle(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.live_map, menu);
        return true;
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

    @Override
    public void onResume() {
        super.onResume();
        //this.updateData();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TripPassagesFragment();
                case 1:
                    return new VehicleRouteFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Stops";
                case 1:
                    return "Map";
                default:
                    return null;
            }
        }
    }
/*
    private void updateData() {
        this.updateData(this.mTripId, this.mVehicleId, this.mMode);
    }
*/
}
