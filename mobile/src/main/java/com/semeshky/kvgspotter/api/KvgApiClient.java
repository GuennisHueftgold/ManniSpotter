package com.semeshky.kvgspotter.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.guennishueftgold.trapezeapi.TrapezeApiClient;
import com.github.guennishueftgold.trapezeapi.TrapezeApiService;

import java.io.File;

import okhttp3.HttpUrl;

public final class KvgApiClient {

    private static KvgApiClient sKvgApiClient;
    protected final TrapezeApiClient mTrapezeApiClient;
    protected final UpdateApiClient mUpdateApiClient;

    protected KvgApiClient(@NonNull Context context) {
        final HttpUrl baseUrl = HttpUrl.parse("http://www.kvg-kiel.de/internetservice/");
        this.mTrapezeApiClient = new TrapezeApiClient(baseUrl, new File(context.getCacheDir(), "trapezeCache"), false);
        this.mUpdateApiClient = new UpdateApiClient(new File(context.getCacheDir(), "localCache"));
    }

    public static void init(@NonNull Context context) {
        sKvgApiClient = new KvgApiClient(context);
    }

    public static TrapezeApiService getService() {
        return sKvgApiClient.mTrapezeApiClient.getService();
    }

    public static UpdateApiService getUpdateService() {
        return sKvgApiClient.mUpdateApiClient.getService();
    }
}
