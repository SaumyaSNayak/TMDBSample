package com.saumya.tmdbsample.network;

import com.google.gson.JsonObject;
import com.saumya.tmdbsample.datamodels.MovieAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {
    @GET("3/movie/popular")
    Call<MovieAPIResponse> getMovieList(@Query("api_key") String api_key);

}
