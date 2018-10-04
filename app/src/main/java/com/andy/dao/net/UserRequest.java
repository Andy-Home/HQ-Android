package com.andy.dao.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andy on 2018/9/28.
 * Modify time 2018/9/28
 */
public interface UserRequest {

    @GET("user/login")
    Observable<Response> login(@Query("data") String data);

    @GET("user/getUserInfo")
    Observable<Response> getUserInfo(@Query("userId") int userId);
}
