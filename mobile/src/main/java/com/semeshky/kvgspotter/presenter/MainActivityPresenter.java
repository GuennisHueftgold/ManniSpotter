package com.semeshky.kvgspotter.presenter;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.EditWidgetActivity;
import com.semeshky.kvgspotter.activities.MainActivity;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStation;

import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

public class MainActivityPresenter {

    private final MainActivity mMainActivity;

    public final ObservableBoolean listContainsItems=new ObservableBoolean(true);
    public MainActivityPresenter(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public void onCreateFavorite() {
        /*
        FavoriteStation favoriteStation = new FavoriteStation();
        favoriteStation.setName("Schauspielhaus");
        favoriteStation.setShortName("151");
        AppDatabase.insertFavoriteStation(favoriteStation)
                .subscribe(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
*/
        this.mMainActivity.startActivity(EditWidgetActivity.createIntent(this.mMainActivity));
    }

    public void onEditFavorite(FavoriteStation favoriteStation) {

    }

    public void onDeleteFavorite(FavoriteStation favoriteStation) {
        AppDatabase
                .deleteFavoriteStation(favoriteStation)
                .subscribe(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Toast.makeText(MainActivityPresenter.this
                                        .mMainActivity,
                                R.string.favorite_deleted, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        Toast.makeText(MainActivityPresenter.this
                                        .mMainActivity,
                                R.string.favorite_deletion_error, Toast.LENGTH_LONG);
                    }
                });
    }
}
