package com.andy.function.catalog;

import android.annotation.SuppressLint;
import android.content.Context;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogPresent implements CatalogContract.Present {

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

    @SuppressLint("CheckResult")
    @Override
    public void getCatalogs(final int parentId, int userId) {

        mCatalogDao.queryCatalogList(parentId)
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
