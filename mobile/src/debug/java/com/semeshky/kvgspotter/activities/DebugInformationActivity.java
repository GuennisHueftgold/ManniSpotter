package com.semeshky.kvgspotter.activities;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.presenter.DebugStopDatabasePresenter;

public class DebugInformationActivity extends AppCompatActivity {

    private ViewDataBinding mBinding;
    private DebugStopDatabasePresenter mStopDatabasePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_debug);
        this.mStopDatabasePresenter = new DebugStopDatabasePresenter();
        this.mBinding.setVariable(BR.stopDatabasePresenter, this.mStopDatabasePresenter);
        AppDatabase
                .getInstance()
                .stopDao()
                .countStopsSync()
                .observe(this,
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer entries) {
                                mStopDatabasePresenter.numberOfEntries.set(entries);
                            }
                        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debug_activity, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateData();
    }

    private void updateData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                this.updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
