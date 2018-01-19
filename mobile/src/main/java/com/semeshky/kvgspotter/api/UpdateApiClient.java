package com.semeshky.kvgspotter.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class UpdateApiClient {
    private final static long CACHE_SIZE = 20 * 1024 * 1024;
    private final static HttpUrl BASE_URL = HttpUrl.parse("https://api.github.com/");
    private final HttpUrl mBaseUrl;
    private final Cache mCache;
    private final OkHttpClient mOkHttpClient;
    private final Gson mGson;
    private final Retrofit mRetrofit;

    public UpdateApiClient(@Nullable File cacheDir) {
        this(BASE_URL, cacheDir);
    }

    public UpdateApiClient(@Nonnull HttpUrl baseUrl, @Nullable File cacheDir) {
        if (baseUrl == null) {
            throw new RuntimeException("BaseUrl must not be null");
        }
        this.mBaseUrl = baseUrl;
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (cacheDir != null) {
            this.mCache = new Cache(cacheDir, UpdateApiClient.CACHE_SIZE);
            okHttpClientBuilder.cache(this.mCache);
        } else {
            this.mCache = null;
        }
        this.mOkHttpClient = okHttpClientBuilder.build();
        this.mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new UpdateApiClientTypeAdapterFactory())
                .create();
        this.mRetrofit = new Retrofit.Builder()
                .client(this.mOkHttpClient)
                .baseUrl(this.mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(this.mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Nonnull
    public UpdateApiService getService() {
        return this.mRetrofit.create(UpdateApiService.class);
    }
}
