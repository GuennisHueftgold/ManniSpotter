package com.semeshky.kvgspotter.activities;

import android.Manifest;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.api.KvgApiClient;
import com.semeshky.kvgspotter.api.Release;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.ActivityMainBinding;
import com.semeshky.kvgspotter.fragments.RequestLocationPermissionDialogFragment;
import com.semeshky.kvgspotter.location.LocationHelper;
import com.semeshky.kvgspotter.util.SemVer;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    protected final static String TAG_ASK_FOR_LOCATION = "ask_for_location";
    protected static final Consumer<Throwable> SILENT_ERROR_CONSUMER = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);
        }
    };
    final static String KEY_LAST_SUCCESSFUL_UPDATE_CHECK = MainActivity.class.getName() + ".last_successful_update_check";
    final static long MINIMUM_UPDATE_DELTA = 6 * 60 * 1000L; // 5 minutes
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
    protected ActivityMainBinding mBinding;
    protected final Consumer<Release> mReleaseConsumer = new Consumer<Release>() {
        @Override
        public void accept(Release release) throws Exception {
            if (release == null)
                return;
            final SemVer current = SemVer.parse(BuildConfig.VERSION_NAME);
            final SemVer onlineVersion = SemVer.parse(release.getTagName());
            if (onlineVersion != null &&
                    current != null &&
                    onlineVersion.isNewer(current)) {
                showUpdateNotice(release);
            }
        }
    };
    protected MainActivityViewModel mViewModel;
    protected HomeAdapter mHomeAdapter;
    protected Disposable mFavoriteDisposable;
    protected LocationHelper mLocationHelper;
    protected Disposable mNearbyDisposable;
    protected long mLastSuccessfulUpdateCheckTimestamp = 0;
    protected Disposable mUpdateCheckDisposable;
    private Disposable mUpdateDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.mViewModel = ViewModelProviders.of(this, new MainActivityViewModel.Factory(AppDatabase.getInstance()))
                .get(MainActivityViewModel.class);
        this.setSupportActionBar((Toolbar) this.mBinding.toolbar.getRoot());
        this.mHomeAdapter = new HomeAdapter(this, this.mFavoriteSelectedListener);
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.recyclerView.setAdapter(this.mHomeAdapter);
        this.mBinding.executePendingBindings();
        this.mLocationHelper = new LocationHelper(this);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_LAST_SUCCESSFUL_UPDATE_CHECK, this.mLastSuccessfulUpdateCheckTimestamp);
    }

    @Override
    public void onRestoreInstanceState(Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        this.mLastSuccessfulUpdateCheckTimestamp = instanceState
                .getLong(KEY_LAST_SUCCESSFUL_UPDATE_CHECK, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outPersistentState
                .putLong(KEY_LAST_SUCCESSFUL_UPDATE_CHECK, this.mLastSuccessfulUpdateCheckTimestamp);
    }

    @Override
    public void onRestoreInstanceState(Bundle instanceState, PersistableBundle persistableBundle) {
        super.onRestoreInstanceState(instanceState, persistableBundle);
        this.mLastSuccessfulUpdateCheckTimestamp = persistableBundle
                .getLong(KEY_LAST_SUCCESSFUL_UPDATE_CHECK, this.mLastSuccessfulUpdateCheckTimestamp);
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

    protected void checkForUpdates() {
        if (System.currentTimeMillis() - MINIMUM_UPDATE_DELTA < this.mLastSuccessfulUpdateCheckTimestamp) {
            return;
        }
        this.mUpdateCheckDisposable = KvgApiClient
                .getUpdateService()
                .getLatestReleaseSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.mReleaseConsumer, SILENT_ERROR_CONSUMER);
    }

    protected void showUpdateNotice(@NonNull final Release release) {
        Snackbar updateSnackbar = Snackbar
                .make(this.mBinding.coordinatorLayout,
                        R.string.update_available,
                        Snackbar.LENGTH_LONG);
        updateSnackbar.setAction(R.string.update, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(release.getHtmlUrl()));
                startActivity(browserIntent);
            }
        });
        updateSnackbar.show();
    }

    protected void requestLocationPermission() {
        this.requestLocationPermission(true);
    }

    protected void requestLocationPermission(boolean showDialog) {
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

    @Override
    public void onResume() {
        super.onResume();
        Flowable<List<HomeAdapter.DistanceStop>> favoriteFlowable;
        if (LocationHelper.hasLocationPermission(this)) {
            this.mHomeAdapter.setHasLocationPermission(true);
            Flowable<Location> t = Flowable.interval(0, 1, TimeUnit.SECONDS)
                    .map(new Function<Long, Location>() {
                        @Override
                        public Location apply(Long aLong) throws Exception {
                            Location location = new Location(LocationManager.GPS_PROVIDER);
                            location.setLongitude(10.1311577 + (Math.random() * 0.001));
                            location.setLatitude(54.3302802 + (Math.random() * 0.001));
                            return location;
                        }
                    });
            final Flowable<List<HomeAdapter.DistanceStop>> favorites = Flowable.combineLatest(t,
                    AppDatabase.getInstance().favoriteSationDao().getAllWithNameFlow(),
                    new BiFunction<Location, List<FavoriteStationWithName>, List<HomeAdapter.DistanceStop>>() {
                        @Override
                        public List<HomeAdapter.DistanceStop> apply(Location location, List<FavoriteStationWithName> favoriteStationWithNames) throws Exception {
                            return MainActivityViewModel.transformFavorites(favoriteStationWithNames, location);
                        }
                    });
            final Flowable<List<HomeAdapter.DistanceStop>> nearby = Flowable.combineLatest(t,
                    AppDatabase.getInstance().stopDao().getAllFlow(),
                    new BiFunction<Location, List<Stop>, List<HomeAdapter.DistanceStop>>() {
                        @Override
                        public List<HomeAdapter.DistanceStop> apply(Location location, List<Stop> favoriteStationWithNames) throws Exception {
                            return MainActivityViewModel.transform(favoriteStationWithNames, location);
                        }
                    }).map(MainActivityViewModel.DISTANCE_STOP_SORT_SHORT_FUNC);
            this.mUpdateDisposable = Flowable.combineLatest(
                    favorites,
                    nearby,
                    new BiFunction<List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>[]>() {
                        @Override
                        public List<HomeAdapter.DistanceStop>[] apply(List<HomeAdapter.DistanceStop> distanceStops, List<HomeAdapter.DistanceStop> distanceStops2) throws Exception {
                            return new List[]{distanceStops, distanceStops2};
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<HomeAdapter.DistanceStop>[]>() {
                        @Override
                        public void accept(List<HomeAdapter.DistanceStop>[] lists) throws Exception {
                            mHomeAdapter.setFavorites(lists[0], false);
                            mHomeAdapter.setNearby(lists[1], true);
                        }
                    });
        } else {
            this.mHomeAdapter.setHasLocationPermission(false);
            this.mUpdateDisposable = this.mViewModel.getFavoriteFlowable(null)
                    .subscribe(new Consumer<List<HomeAdapter.DistanceStop>>() {
                        @Override
                        public void accept(List<HomeAdapter.DistanceStop> distanceStops) throws Exception {
                            mHomeAdapter.setFavorites(distanceStops);
                        }
                    }, SILENT_ERROR_CONSUMER);
        }
        this.checkForUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mFavoriteDisposable != null && !this.mFavoriteDisposable.isDisposed())
            this.mFavoriteDisposable.dispose();
        if (this.mNearbyDisposable != null && !this.mNearbyDisposable.isDisposed())
            this.mNearbyDisposable.dispose();
        if (this.mUpdateCheckDisposable != null && !this.mUpdateCheckDisposable.isDisposed())
            this.mUpdateCheckDisposable.dispose();
        if (this.mUpdateDisposable != null && !this.mUpdateDisposable.isDisposed()) {
            this.mUpdateDisposable.dispose();
        }
    }

    protected void showRequestPermissionDialog() {
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
        dialog.show(this.getSupportFragmentManager(), TAG_ASK_FOR_LOCATION);
    }
}
