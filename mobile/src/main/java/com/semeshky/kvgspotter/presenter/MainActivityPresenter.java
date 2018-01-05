package com.semeshky.kvgspotter.presenter;

import android.databinding.ObservableBoolean;

import com.semeshky.kvgspotter.activities.MainActivity;

public class MainActivityPresenter {

    public final ObservableBoolean listContainsItems=new ObservableBoolean(true);
    private final MainActivity mMainActivity;
    public MainActivityPresenter(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

}
