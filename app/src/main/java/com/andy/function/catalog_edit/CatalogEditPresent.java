package com.andy.function.catalog_edit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.Catalog;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogEditPresent implements CatalogEditContract.Present, LifecycleObserver {
    private Context mContext;
    private CatalogEditContract.View mView;

    CatalogEditPresent(Context context, CatalogEditContract.View view) {
        mContext = context;
        mView = view;
        init();
    }

    private DaoManager mDaoManager;
    private void init() {
        mDaoManager = DaoManager.getInstance();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mView = null;
    }

    @Override
    public void getCatalog(int id) {
        mDaoManager.mCatalogService.getCatalog(id, new BaseListener<Catalog>() {
            @Override
            public void onSuccess(Catalog o) {
                if (mView != null) {
                    mView.displayCatalog(o);
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

    @Override
    public void save(final Catalog catalog) {
        mDaoManager.mCatalogService.newCatalog(catalog, new BaseListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                if (mView != null) {
                    mView.saveSuccess();
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

    @Override
    public void delete(final Catalog catalog) {
        mDaoManager.mCatalogService.deleteCatalog(catalog, new BaseListener<Object>() {

            @Override
            public void onSuccess(Object o) {
                if (mView != null) {
                    mView.deleteSuccess();
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
