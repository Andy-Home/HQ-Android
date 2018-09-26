package com.andy.dao.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatalogRequest {

    @GET("catalog/getCatalogs")
    Observable<Response> getCatalogs(@Query("catalogId") int catalogId, @Query("userId") int userId);

    @GET("catalog/sync")
    Observable<Response> sync(@Query("userId") int userId);

    @GET("catalog/syncCallback")
    Observable<Response> syncCallback(@Query("catalogId") int... catalogId);

    @GET("catalog/new")
    Observable<Response> newCatalog(@Query("data") String data);

    @GET("catalog/update")
    Observable<Response> updateCatalog(@Query("data") String data);

    @GET("catalog/delete")
    Observable<Response> deleteCatalog(@Query("catalogId") int catalogId, @Query("userId") int userId);
}
