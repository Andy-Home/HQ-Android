package com.andy.dao.net;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetRequestManager {
    private NetRequestManager() {
        init();
    }

    public static NetRequestManager getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        final static NetRequestManager mInstance = new NetRequestManager();
    }

    private Retrofit mRetrofit;

    private void init() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://www.andyclanguage.cn/HQ")
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
}
