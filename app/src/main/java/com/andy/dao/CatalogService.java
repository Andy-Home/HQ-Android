package com.andy.dao;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CatalogService {
    private CatalogService() {
        init();
    }

    public static CatalogService getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        final static CatalogService mInstance = new CatalogService();
    }

    private CatalogDao mCatalogDao;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
    }

    //TODO:网络请求加数据库请求
    private Disposable mGetCatalogs = null;

    public List<Catalog> getCatalogList(int parentId) {
        mGetCatalogs = mCatalogDao.queryCatalogList(parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Catalog>>() {
                    @Override
                    public void accept(List<Catalog> catalogs) {
                        //mView.displayCatalogs(new ArrayList<>(catalogs));
                    }
                });
        return null;
    }
}
