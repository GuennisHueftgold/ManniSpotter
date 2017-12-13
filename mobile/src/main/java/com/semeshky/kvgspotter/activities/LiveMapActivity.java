package com.semeshky.kvgspotter.activities;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.ActivityLiveMapBinding;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

public class LiveMapActivity extends AppCompatActivity {

    protected ActivityLiveMapBinding mBinding;
    protected ActivityLiveMapViewModel mViewModel;

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
        this.setSupportActionBar(this.mBinding.toolbar);
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
