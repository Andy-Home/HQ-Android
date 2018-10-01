package com.andy.dao.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andy on 2018/9/27.
 * Modify time 2018/9/27
 */
public interface RecordRequest {

    @GET("record/sync")
    Observable<Response> sync(@Query("userId") int userId, @Query("startTime") long startTime, @Query("endTime") long endTime);

    @GET("record/getRecords")
    Observable<Response> getRecordList(@Query("userId") int userId, @Query("time") long time);

    @GET("record/new")
    Observable<Response> newRecord(@Query("data") String data);

    @GET("record/update")
    Observable<Response> updateRecord(@Query("data") String data);

    @GET("record/delete")
    Observable<Response> deleteRecord(@Query("recordId") int recordId, @Query("userId") int userId);
}
