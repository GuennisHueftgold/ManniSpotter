package com.semeshky.kvgspotter.map;

import android.content.Context;

import com.semeshky.kvgspotter.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.DefaultConfigurationProvider;

import java.io.File;

import timber.log.Timber;

public final class OsmConfiguration extends DefaultConfigurationProvider {

    public OsmConfiguration(Context context){
        super();
        this.setMapViewHardwareAccelerated(true);
        this.setUserAgentValue("KvgSpotter-Agent/"+ BuildConfig.VERSION_NAME);
        final File osmCacheDir=new File(context.getCacheDir(),"osm");
        final File osmCacheTileDir=new File(context.getCacheDir(),"osm/tiles");
        this.setOsmdroidBasePath(osmCacheDir);
        this.setOsmdroidTileCache(osmCacheTileDir);
    }
}
