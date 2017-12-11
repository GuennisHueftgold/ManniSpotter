package com.semeshky.kvg.kvgapi;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KvgApiClient {

    public final static double COORDINATES_CONVERTION_CONSTANT = 3600000d;
    private final static long CACHE_SIZE = 1024 * 1024 * 20;
    private final static String API_BASE_URL = "http://www.kvg-kiel.de/internetservice/";
    private static KvgApiClient mInstance;
    private final OkHttpClient mOkHttpClient;
    private final Retrofit mRetrofit;
    private final Gson mGson;
    private final Cache mCache;

    public KvgApiClient(Context applicationContext) {
        final File cacheDir = new File(applicationContext.getCacheDir(), "httpCache");
        this.mCache = new Cache(cacheDir, KvgApiClient.CACHE_SIZE);
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .cache(this.mCache)
                .addNetworkInterceptor(new CacheManipulatorInterceptor())
                .addNetworkInterceptor(new AutocompleteConverterInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            okHttpClientBuilder.addNetworkInterceptor(logging);
        }
        this.mOkHttpClient = okHttpClientBuilder.build();
        this.mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new KvgApiTypeAdapterFactory())
                .create();
        this.mRetrofit = new Retrofit.Builder()
                .client(this.mOkHttpClient)
                .baseUrl(KvgApiClient.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(this.mGson))
                .build();
    }

    public static KvgApiClient getInstance() {
        return KvgApiClient.mInstance;
    }

    public final static void init(@NonNull Context context) {
        if (KvgApiClient.mInstance != null) {
            return;
        }
        KvgApiClient.mInstance = new KvgApiClient(context.getApplicationContext());
    }

    public KvgApiService getService() {
        return this.mRetrofit.create(KvgApiService.class);
    }
}
