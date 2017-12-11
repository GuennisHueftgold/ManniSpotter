package com.semeshky.kvgspotter;

import com.semeshky.kvgspotter.map.OsmConfiguration;

import org.osmdroid.config.Configuration;

public final class OsmKvgSpotterApplication extends KvgSpotterApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.setConfigurationProvider(new OsmConfiguration(this));
    }
}
