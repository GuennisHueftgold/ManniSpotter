package com.semeshky.kvgspotter.activities;


import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
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
            this.mBottomSheetBehavior = BottomSheetBehavior.from(this.mBinding.bottomSheet);
            this.mBottomSheetBehavior.setPeekHeight(300);
            this.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            this.mBottomSheetBehavior.setHideable(true);
            this.mBottomSheetBehavior
                    .setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                // Update the ViewHolder State if the Sheet was Dismissed
                                LiveMapActivity
                                        .this
                                        .mViewModel
                                        .setDetailsStatus(ActivityLiveMapViewModel.DETAILS_STATUS_CLOSED);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });
        }
        final SearchView searchView = this.mBinding.searchView;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, StationSearchActivity.class)));

        // prevent autofocus of searchview
        this.mBinding.getRoot().requestFocus();

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
                        switch (integer) {
                            case ActivityLiveMapViewModel.DETAILS_STATUS_SHOW_STOP:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detailsFragment, new LiveMapDepartureFragment(), "departure_fragment")
                                        .commit();
                                LiveMapActivity
                                        .this
                                        .setDetailsVisible(true);
                                break;
                            case ActivityLiveMapViewModel.DETAILS_STATUS_SHOW_TRIP:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detailsFragment, new LiveMapPassagesFragment(), "transit_fragment")
                                        .commit();
                                LiveMapActivity
                                        .this
                                        .setDetailsVisible(false);
                                break;
                            default:
                                LiveMapActivity
                                        .this
                                        .setDetailsVisible(false);
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
        //this.mViewModel.updateData();
        this.mViewModel.startVehicleLocationUpdater();
    }

    @Override
    public void onPause() {
        this.mViewModel.stopVehicleLocationUpdater();
        super.onPause();
    }

    /**
     * Hides or shows the Details Pane for selected Markers on the map
     *
     * @param detailsVisible
     */
    public void setDetailsVisible(boolean detailsVisible) {
        /**
         * We are on a smaller device so we need to handle the bottom sheet
         */
        if (this.mBottomSheetBehavior != null) {
            this.mBottomSheetBehavior.setState(detailsVisible ?
                    BottomSheetBehavior.STATE_EXPANDED :
                    BottomSheetBehavior.STATE_HIDDEN);
        } else {
            // Select details fragment container CardView
            final View view = (View) findViewById(R.id.detailsFragment).getParent();
            view.setVisibility(detailsVisible ?
                    View.VISIBLE :
                    View.GONE);
        }
    }
}
