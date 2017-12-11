package com.semeshky.kvgspotter;

import timber.log.Timber;


public class KvgSpotterApplication extends AbstractKvgSpotterApplication {

    @Override
    public void onCreate() {
        Timber.plant(new Timber.DebugTree());
        super.onCreate();
    }
}
