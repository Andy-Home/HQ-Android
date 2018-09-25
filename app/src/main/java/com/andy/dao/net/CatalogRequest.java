package com.andy.dao.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatalogRequest {

    @GET("catalog/getCatalogs")
    Call<Response> getCatalogs(@Query("catalogId") int catalogId, @Query("userId") int userId);

    @GET("catalog/sync")
    Call<Response> sync(@Query("userId") int userId);

    @GET("catalog/syncCallback")
    Call<Response> syncCallback(@Query("catalogId") int... catalogId);

    @GET("catalog/new")
    Call<Response> newCatalog(@Query("data") String data);

    @GET("catalog/update")
    Call<Response> updateCatalog(@Query("data") String data);

    @GET("catalog/delete")
    Call<Response> deleteCatalog(@Query("catalogId") int catalogId, @Query("userId") int userId);
}
