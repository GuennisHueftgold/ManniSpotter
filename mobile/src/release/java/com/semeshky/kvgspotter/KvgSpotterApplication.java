package com.semeshky.kvgspotter;

import android.app.Application;

import com.semeshky.kvg.kvgapi.KvgApiClient;

import timber.log.Timber;


public class KvgSpotterApplication extends AbstractKvgSpotterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
