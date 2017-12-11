package com.semeshky.kvgspotter.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.database.FavoriteStation;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.databinding.ActivityMainBinding;
import com.semeshky.kvgspotter.presenter.MainActivityPresenter;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final HomeAdapter.OnFavoriteSelectListener mFavoriteSelectedListener = new HomeAdapter.OnFavoriteSelectListener() {
        @Override
        public void onFavoriteSelected(FavoriteStation favoriteStation) {
            final Intent intent = StationDetailActivity.createIntent(MainActivity.this, favoriteStation.getShortName());
            startActivity(intent);
        }
    };
    private ActivityMainBinding mBinding;
    private MainActivityPresenter mMainActivityPresenter;
    private MainActivityViewModel mViewModel;
    private HomeAdapter mHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.mViewModel = ViewModelProviders.of(this)
                .get(MainActivityViewModel.class);
        this.setSupportActionBar((Toolbar) this.mBinding.toolbar);
        this.mMainActivityPresenter = new MainActivityPresenter(this);
        this.mHomeAdapter = new HomeAdapter(this.mFavoriteSelectedListener);
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.recyclerView.setAdapter(this.mHomeAdapter);
        this.mBinding.setPresenter(this.mMainActivityPresenter);
        this.mBinding.executePendingBindings();
        this.mViewModel
                .getFavoriteStations()
                .observe(this, new Observer<List<FavoriteStationWithName>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteStationWithName> favoriteStations) {
                        MainActivity
                                .this
                                .mHomeAdapter
                                .setItems(favoriteStations);
                    }
                });
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
}
