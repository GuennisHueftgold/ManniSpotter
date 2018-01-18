package com.semeshky.kvgspotter.api;

import retrofit2.Call;
import retrofit2.http.GET;

interface UpdateApiService {
    @GET("repos/GuennisHueftgold/ManniSpotter/releases/latest")
    Call<Release> getLatestRelease();
}
