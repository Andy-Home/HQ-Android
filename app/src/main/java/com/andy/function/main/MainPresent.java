package com.andy.function.main;

import android.content.Context;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;

public class MainPresent implements MainContract.Present {

    private Context mContext;
    private MainContract.View mView;

    public MainPresent(Context context, MainContract.View view) {
        mContext = context;
        mView = view;
        init();
    }

    private DaoManager mDaoManager;

    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initCatalogService();
        mDaoManager.initRecordService();
    }

    @Override
    public void syncCatalog() {
        mDaoManager.mCatalogService.sync(new BaseListener<Object>() {

            @Override
            public void onSuccess(Object o) {
                mView.syncCatalogSuccess();
            }

            @Override
            public void onError(String msg) {
                mView.onError(msg);
            }
        });
    }

    @Override
    public void syncRecord() {
        mDaoManager.mRecordService.sync(new BaseListener<Object>() {

            @Override
            public void onSuccess(Object o) {
                mView.syncRecordSuccess();
            }

            @Override
            public void onError(String msg) {
                mView.onError(msg);
            }
        });
    }
}
