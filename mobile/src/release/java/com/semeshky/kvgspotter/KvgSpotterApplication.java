package com.semeshky.kvgspotter;

import android.app.Application;

import com.github.guennishueftgold.trapezeapi.KvgApiClient;

import timber.log.Timber;


public class KvgSpotterApplication extends AbstractKvgSpotterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
