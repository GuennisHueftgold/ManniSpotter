package com.semeshky.kvgspotter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.viewmodel.DebugInformationActivityViewModel;

public class DebugInformationActivity extends AppCompatActivity {

    private ViewDataBinding mBinding;
    private DebugInformationActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_debug);
        this.mViewModel = ViewModelProviders.of(this)
                .get(DebugInformationActivityViewModel.class);
        this.mBinding.setVariable(BR.viewModel, this.mViewModel);
        AppDatabase
                .getInstance()
                .stopDao()
                .countStopsSync()
                .observe(this,
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer entries) {
                                mViewModel.stops.set(entries);
                            }
                        });
        AppDatabase
                .getInstance()
                .stopPointDao()
                .countStopsSync()
                .observe(this,
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer entries) {
                                mViewModel.stopPoints.set(entries);
                            }
                        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debug_activity, menu);
        return true;
    }

}
