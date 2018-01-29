package com.semeshky.kvgspotter.fragments;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.TextView;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationSearchActivity;
import com.semeshky.kvgspotter.adapter.SetupFavoriteAdapter;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStation;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Ask Location Permission
 */
public class SetupStep4Fragment extends Fragment {
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SetupFavoriteAdapter mAdapter;
    private TextView mTxtDescription;
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
        this.mTxtDescription = view.findViewById(R.id.txtDescription);
        this.mAdapter = new SetupFavoriteAdapter();
        final Context context = view.getContext();
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(context, StationSearchActivity.class)));
        this.mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                final String shortName = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));
                favoriteStation(shortName)
                        .subscribe(new DisposableSingleObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                return true;
            }
        });
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mRecyclerView.setAdapter(this.mAdapter);
        AppDatabase.getInstance()
                .favoriteSationDao()
                .getAllWithNameSync()
                .observe(this, new Observer<List<FavoriteStationWithName>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteStationWithName> favoriteStationWithNames) {
                        SetupStep4Fragment
                                .this
                                .setFavoriteStations(favoriteStationWithNames);
                    }
                });
    }

    public Single<Boolean> favoriteStation(final String shortName) {
        if (shortName == null) {
            return Single.just(false);
        }
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                final FavoriteStation favoriteStation = new FavoriteStation();
                favoriteStation.setShortName(shortName);
                final long result = AppDatabase
                        .getInstance()
                        .favoriteSationDao()
                        .insert(favoriteStation);
                emitter.onSuccess(result >= 0);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> removeFavorite(final String shortName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                final int result = AppDatabase
                        .getInstance()
                        .favoriteSationDao()
                        .delete(shortName);
                emitter.onSuccess(result == 1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void setFavoriteStations(List<FavoriteStationWithName> favoriteStations) {
        final boolean listIsEmpty = (favoriteStations == null || favoriteStations.size() == 0);
        this.mTxtDescription.setVisibility(listIsEmpty ? View.VISIBLE : View.GONE);
        this.mRecyclerView.setVisibility(listIsEmpty ? View.GONE : View.VISIBLE);
        if (!listIsEmpty) {
            this.mAdapter.setItems(favoriteStations);
            this.mRecyclerView.scrollToPosition(0);
        }
    }
}
