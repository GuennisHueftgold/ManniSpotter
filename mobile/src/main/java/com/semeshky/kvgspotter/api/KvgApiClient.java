package com.semeshky.kvgspotter.api;

import android.content.Context;

import com.github.guennishueftgold.trapezeapi.TrapezeApiClient;
import com.github.guennishueftgold.trapezeapi.TrapezeApiService;

import java.io.File;

import okhttp3.HttpUrl;

public final class KvgApiClient {

    private static TrapezeApiClient sTrapezeApiClient;

    public static void init(Context context) {
        final HttpUrl baseUrl = HttpUrl.parse("http://www.kvg-kiel.de/internetservice/");
        sTrapezeApiClient = new TrapezeApiClient(baseUrl, new File(context.getCacheDir(), "trapezeCache"), false);
    }

    public static TrapezeApiService getService() {
        return sTrapezeApiClient.getService();
    }
}
