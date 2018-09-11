package com.andy.function.main.chart;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.RecordDao;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.RecordStatistics;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    public void getCatalog(final long start, final long end, int id) {
        final List<RecordStatistics> data = new ArrayList<>();

        mGetCatalog = mCatalogDao.queryCatalogList(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<Catalog>, Publisher<Catalog>>() {
                    @Override
                    public Publisher<Catalog> apply(List<Catalog> catalogs) {
                        return Flowable.fromIterable(catalogs);
                    }
                }).map(new Function<Catalog, Map<String, Object>>() {

                    @Override
                    public Map<String, Object> apply(final Catalog catalog) {
                        return mCatalogDao.queryCatalogAll(catalog.id)
                                .observeOn(Schedulers.io())
                                .map(new Function<List<Catalog>, Map<String, Object>>() {

                                    @Override
                                    public Map<String, Object> apply(List<Catalog> catalogs) {
                                        Integer[] data = new Integer[catalogs.size()];
                                        for (int i = 0; i < catalogs.size(); i++) {
                                            data[i] = catalogs.get(i).id;
                                        }
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("data", data);
                                        map.put("catalog", catalog);
                                        return map;
                                    }
                                }).blockingFirst();
                    }
                }).map(new Function<Map<String, Object>, RecordStatistics>() {

                    @Override
                    public RecordStatistics apply(Map<String, Object> stringObjectMap) {
                        Integer[] data = (Integer[]) stringObjectMap.get("data");
                        final Catalog catalog = (Catalog) stringObjectMap.get("catalog");
                        return mRecordDao.queryRecordTotal(start, end, data)
                                .observeOn(Schedulers.io())
                                .map(new Function<Double, RecordStatistics>() {

                                    @Override
                                    public RecordStatistics apply(Double aDouble) {
                                        RecordStatistics recordStatistics = new RecordStatistics();
                                        recordStatistics.catalog = catalog.id;
                                        recordStatistics.catalogName = catalog.name;
                                        recordStatistics.type = catalog.style;
                                        recordStatistics.num = aDouble;
                                        return recordStatistics;
                                    }
                                }).blockingGet();
                    }
                }).subscribe(new Consumer<RecordStatistics>() {
                    @Override
                    public void accept(RecordStatistics recordStatistics) {
                        data.add(recordStatistics);
                    }
                });
    }
}
