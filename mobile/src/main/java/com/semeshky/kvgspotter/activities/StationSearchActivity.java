package com.semeshky.kvgspotter.activities;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.UriMatcher;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.github.guennishueftgold.trapezeapi.FulltextSearchResult;
import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.ActivitySearchBinding;
import com.semeshky.kvgspotter.viewmodel.SearchActivityViewModel;

public class StationSearchActivity extends AppCompatActivity {
    private final static int STOP_FILTER = 1;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(BuildConfig.APPLICATION_ID, "stop/*", STOP_FILTER);
    }

    private ActivitySearchBinding mBinding;
    private SearchActivityViewModel mSearchViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        this.mSearchViewModel = ViewModelProviders.of(this)
                .get(SearchActivityViewModel.class);
        this.mSearchViewModel.setOnSearchResultClickListener(
                new SearchActivityViewModel.OnSearchResultClickListener() {
                    @Override
                    public void onSearchResultSelected(FulltextSearchResult fulltextSearchResult) {
                        startActivity(StationDetailActivity
                                .createIntent(StationSearchActivity.this,
                                        fulltextSearchResult));
                    }
                }
        );
        this.mBinding.setVariable(BR.searchViewModel, this.mSearchViewModel);
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.recyclerView.setAdapter(this.mSearchViewModel.searchResultAdapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(@NonNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            final Uri data = intent.getData();
            showResult(data);
        }
    }

    private void showResult(Uri data) {
        switch (sURIMatcher.match(data)) {
            case STOP_FILTER:
                final Intent intent = StationDetailActivity.createIntent(this, data.getLastPathSegment());
                this.finish();
                this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void doMySearch(final String query) {
        this.mSearchViewModel.searchStation(query);
    }
}
