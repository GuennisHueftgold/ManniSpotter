package com.semeshky.kvgspotter.fragments;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationSearchActivity;
import com.semeshky.kvgspotter.adapter.SetupFavoriteAdapter;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.List;

/**
 * Ask Location Permission
 */
public class SetupStep4Fragment extends Fragment {
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SetupFavoriteAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_step_4, container, false);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mSearchView = view.findViewById(R.id.searchView);
        this.mRecyclerView = view.findViewById(R.id.recyclerView);
        this.mAdapter = new SetupFavoriteAdapter();
        final Context context = view.getContext();
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(context, StationSearchActivity.class)));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mRecyclerView.setAdapter(this.mAdapter);
        AppDatabase.getInstance()
                .favoriteSationDao()
                .getAllWithNameSync()
                .observe(this, new Observer<List<FavoriteStationWithName>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteStationWithName> favoriteStationWithNames) {
                        if (favoriteStationWithNames == null) {
                            return;
                        }
                        mAdapter.setItems(favoriteStationWithNames);
                    }
                });
    }

}
