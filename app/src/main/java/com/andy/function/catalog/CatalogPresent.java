package com.andy.function.catalog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.Catalog;

import java.util.ArrayList;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogPresent implements CatalogContract.Present, LifecycleObserver {

    private Context mContext;

    private CatalogContract.View mView;

    CatalogPresent(Context context, CatalogContract.View views) {
        mContext = context;
        mView = views;
        init();
    }

    private DaoManager mDaoManager = null;
    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initCatalogService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mView = null;
    }

    @Override
    public void getCatalogs(final int parentId) {
        mDaoManager.mCatalogService.getCatalogList(parentId, new BaseListener<ArrayList<Catalog>>() {
            @Override
            public void onSuccess(ArrayList<Catalog> data) {
                if (mView != null) {
                    mView.displayCatalogs(data);
                }
            }

            @Override
            public void onError(String msg) {
                if (mView != null) {
                    mView.onError(msg);
                }
            }
        });
    }
}
