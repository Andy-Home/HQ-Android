package com.andy.function.main.chart;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.RecordDao;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.RecordStatistics;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class ChartPresent implements ChartContract.Present, LifecycleObserver {
    private ChartContract.View mView;

    public ChartPresent(ChartContract.View view) {
        mView = view;
        init();
    }

    private RecordDao mRecordDao;
    private CatalogDao mCatalogDao;

    private void init() {
        DBManage dbManage = DBManage.getInstance();

        mRecordDao = dbManage.getRecordDao();
        mCatalogDao = dbManage.getCatalogDao();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mGetCatalog != null && !mGetCatalog.isDisposed()) {
            mGetCatalog.dispose();
        }
    }

    private Disposable mGetCatalog = null;

    @Override
    public void getCatalog(long start, long end, int id) {
        mGetCatalog = mRecordDao.queryRecordStatistics(start, end, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecordStatistics>>() {
                    @Override
                    public void accept(List<RecordStatistics> recordStatistics) {
                        mView.displayChart();
                    }
                });

        mCatalogDao.queryCatalogAll(0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Catalog>>() {
                    @Override
                    public void accept(List<Catalog> catalogs) {
                        mView.displayChart();
                    }
                });

    }
}
