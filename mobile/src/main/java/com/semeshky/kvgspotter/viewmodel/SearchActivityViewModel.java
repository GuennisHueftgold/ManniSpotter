package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.IntDef;
import android.support.v4.widget.SwipeRefreshLayout;

import com.github.guennishueftgold.trapezeapi.FulltextSearch;
import com.github.guennishueftgold.trapezeapi.FulltextSearchResult;
import com.semeshky.kvgspotter.adapter.SearchResultAdapter;
import com.semeshky.kvgspotter.api.KvgApiClient;

import java.lang.annotation.Retention;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SearchActivityViewModel extends ViewModel implements SwipeRefreshLayout.OnRefreshListener {
    public final static int STATUS_FAILED = 1, STATUS_SEARCHING = 2, STATUS_FOUND = 3;
    public final ObservableField<String> filterText =
            new ObservableField<>();
    public final ObservableInt searchStatus =
            new ObservableInt();
    public final SearchResultAdapter searchResultAdapter;

    public SearchActivityViewModel() {
        super();
        this.searchResultAdapter = new SearchResultAdapter();
    }

    public void setOnSearchResultClickListener(OnSearchResultClickListener listener) {
        this.searchResultAdapter.setOnSearchResultClickListener(listener);
    }

    @Override
    public void onRefresh() {
        this.searchStation(this.filterText.get());
    }

    public void searchStation(final String query) {
        this.filterText.set(query);
        this.searchStatus.set(STATUS_SEARCHING);
        KvgApiClient
                .getService()
                .getStopsByName(query)
                .enqueue(new Callback<FulltextSearch>() {
                    @Override
                    public void onResponse(Call<FulltextSearch> call, Response<FulltextSearch> response) {
                        SearchActivityViewModel.this
                                .searchStatus.set(STATUS_FOUND);
                        SearchActivityViewModel.this
                                .searchResultAdapter.setItems(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<FulltextSearch> call, Throwable t) {
                        SearchActivityViewModel.this
                                .searchStatus.set(STATUS_FAILED);
                    }
                });
    }

    public interface OnSearchResultClickListener {
        void onSearchResultSelected(FulltextSearchResult fulltextSearchResult);
    }

    @Retention(SOURCE)
    @IntDef({STATUS_FAILED, STATUS_SEARCHING, STATUS_FOUND})
    public @interface Status {
    }
}
