package com.andy.function.catalog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogPresent implements CatalogContract.Present, LifecycleObserver {

    private Context mContext;

    private CatalogContract.View mView;

    public CatalogPresent(Context context, CatalogContract.View views) {
        mContext = context;
        mView = views;
        init();
    }

    private CatalogDao mCatalogDao;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mGetCatalogs != null && !mGetCatalogs.isDisposed()) {
            mGetCatalogs.dispose();
        }
    }

    private Disposable mGetCatalogs = null;
    @Override
    public void getCatalogs(final int parentId, int userId) {

        mGetCatalogs = mCatalogDao.queryCatalogList(parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Catalog>>() {
                    @Override
                    public void accept(List<Catalog> catalogs) throws Exception {
                        mView.displayCatalogs(new ArrayList<>(catalogs));
                    }
                });
    }
}
