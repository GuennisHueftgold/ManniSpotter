package com.semeshky.kvgspotter.activities;


import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.ActivityLiveMapBinding;
import com.semeshky.kvgspotter.fragments.LiveMapDepartureFragment;
import com.semeshky.kvgspotter.fragments.LiveMapPassagesFragment;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import timber.log.Timber;

public class LiveMapActivity extends AppCompatActivity {

    protected ActivityLiveMapBinding mBinding;
    protected ActivityLiveMapViewModel mViewModel;
    private BottomSheetBehavior<FrameLayout> mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_live_map);
        this.mViewModel = ViewModelProviders.of(this).get(ActivityLiveMapViewModel.class);
        if (this.mBinding.bottomSheet != null) {
            mBottomSheetBehavior = BottomSheetBehavior.from(this.mBinding.bottomSheet);
            mBottomSheetBehavior.setPeekHeight(300);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetBehavior.setHideable(true);
        }
        final SearchView searchView = this.mBinding.searchView;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, StationSearchActivity.class)));

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
            }
        });

        final View bottomBox = this.mBinding.bottomSheet;
        if (bottomBox != null) {
            final View searchBox = this.mBinding.getRoot();
            searchBox.getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    final int pWidth = searchBox.getWidth();
                                    final int pHeight = searchBox.getHeight();
                                    final int offset = pWidth * 9 / 16;
                                    bottomBox.getLayoutParams().width = pWidth;
                                    bottomBox.getLayoutParams().height = pHeight - offset;
//                                bottomBox.setLayoutParams(new CoordinatorLayout.LayoutParams(pWidth,pHeight-offset));
                                    //ViewCompat.offsetTopAndBottom(bottomBox,offset);
                                }
                            }
                    );
            searchBox.clearFocus();
            this.mBinding.getRoot().requestFocus();
            this.mViewModel.getSelectedStop()
                    .observe(this, new Observer<Stop>() {
                        @Override
                        public void onChanged(@Nullable Stop stop) {
                            Timber.d("stop clicked: %s", stop.toString());
                            if (stop == null) {
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            } else {
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        }
                    });

        }
        this.mViewModel
                .getDetailsStatus()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        final View view = (View) findViewById(R.id.detailsFragment).getParent();
                        switch (integer) {
                            case ActivityLiveMapViewModel.DETAILS_STATUS_SHOW_STOP:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detailsFragment, new LiveMapDepartureFragment(), "departure_fragment")
                                        .commit();
                                view.setVisibility(View.VISIBLE);
                                break;
                            case ActivityLiveMapViewModel.DETAILS_STATUS_SHOW_TRIP:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detailsFragment, new LiveMapPassagesFragment(), "transit_fragment")
                                        .commit();
                                view.setVisibility(View.VISIBLE);
                                break;
                            default:
                                view.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.live_map, menu);
        return true;
    }

    @Override
    public void onResume() {
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
