package com.semeshky.kvgspotter.api;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UpdateApiService {
    @GET("repos/GuennisHueftgold/ManniSpotter/releases/latest")
    Call<Release> getLatestRelease();

    @GET("repos/GuennisHueftgold/ManniSpotter/releases/latest")
    Single<Release> getLatestReleaseSingle();
}
