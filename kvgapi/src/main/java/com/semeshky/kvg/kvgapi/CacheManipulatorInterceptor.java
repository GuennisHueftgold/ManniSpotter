package com.semeshky.kvg.kvgapi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;


final class CacheManipulatorInterceptor implements Interceptor {
    private final static long MINUTE=60;
    private final static long HOUR=60*MINUTE;

            private final static String NAME_CACHE_CONTROL="Cache-Control";
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request=chain.request();
        final Response response=chain.proceed(request);
        if(request.method().equalsIgnoreCase("GET")){
            final String encodedPath=request.url().encodedPath();
            if(encodedPath.endsWith(KvgApiService.PATH_PATH_INFO_BY_TRIP_ID)){
                final CacheControl cacheControl=
                        new CacheControl.Builder()
                        .maxAge(1, TimeUnit.HOURS)
                        .minFresh(10, TimeUnit.MINUTES)
                        .build();
                return response
                        .newBuilder()
                        .header(NAME_CACHE_CONTROL,cacheControl.toString())
                        .build();
            }
            if(encodedPath.endsWith(KvgApiService.PATH_STATION_LOCATIONS)){
                final CacheControl cacheControl=
                        new CacheControl.Builder()
                                .maxAge(1, TimeUnit.HOURS)
                                .minFresh(10, TimeUnit.MINUTES)
                                .build();
                return response
                        .newBuilder()
                        .header(NAME_CACHE_CONTROL,cacheControl.toString())
                        .build();
            }
        }
        return response;
    }
}
