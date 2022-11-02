package com.dallmeier.evidencer.network;


import com.dallmeier.evidencer.model.place.Places;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceMap {
    @GET("/search.php?format=json&polygon=1&addressdetails=1")
    Call<List<Places>> filterAddress(@Query("q") String address);
}
