package com.andy.dao.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetRequestManager {
    private NetRequestManager() {
        init();
    }

    public static NetRequestManager getInstance() {
        return SingleHolder.mInstance;
    }

    static class SingleHolder {
        final static NetRequestManager mInstance = new NetRequestManager();
    }

    private Retrofit mRetrofit;

    private void init() {
        mRetrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.6.103:8080/")
                .baseUrl("https://www.andyclanguage.cn/HQ/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    private CatalogRequest mCatalogRequest;

    public CatalogRequest getCatalogRequest() {
        if (mCatalogRequest == null) {
            mCatalogRequest = mRetrofit.create(CatalogRequest.class);
        }
        return mCatalogRequest;
    }

    private RecordRequest mRecordRequest;

    public RecordRequest getRecordRequest() {
        if (mRecordRequest == null) {
            mRecordRequest = mRetrofit.create(RecordRequest.class);
        }
        return mRecordRequest;
    }

    private UserRequest mUserRequest;

    public UserRequest getUserRequest() {
        if (mUserRequest == null) {
            mUserRequest = mRetrofit.create(UserRequest.class);
        }
        return mUserRequest;
    }
}
