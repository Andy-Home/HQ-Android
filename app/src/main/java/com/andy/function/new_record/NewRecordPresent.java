package com.andy.function.new_record;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.RecordDao;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.Record;
import com.andy.function.new_record.entity.CatalogItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/7.
 * Modify time 2018/9/7
 */
public class NewRecordPresent implements NewRecordContract.Present, LifecycleObserver {
    private NewRecordContract.Views mView;

    public NewRecordPresent(NewRecordContract.Views view) {
        mView = view;
        init();
    }

    private CatalogDao mCatalogDao;
    private RecordDao mRecordDao;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
        mRecordDao = DBManage.getInstance().getRecordDao();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mGetCatalog != null && !mGetCatalog.isDisposed()) {
            mGetCatalog.dispose();
        }

        if (mSaveRecord != null && !mSaveRecord.isDisposed()) {
            mSaveRecord.dispose();
        }
    }

    private Disposable mGetCatalog = null;

    @Override
    public void getCatalog(final View view, int id) {
        mGetCatalog = mCatalogDao.queryCatalogList(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Catalog>>() {
                    @Override
                    public void accept(List<Catalog> catalogs) {
                        List<CatalogItem> items = new ArrayList<>();
                        for (Catalog catalog : catalogs) {
                            CatalogItem item = new CatalogItem();
                            item.text = catalog.name;
                            item.id = catalog.id;
                            item.parentId = catalog.parentId;
                            item.type = catalog.style;
                            items.add(item);
                        }
                        mView.showCatalogWindow(view, items);
                    }
                });


    }

    private Disposable mSaveRecord;

    @Override
    public void saveRecord(final Record record) {
        Observable<Integer> observer = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                mRecordDao.insert(record);
                emitter.onNext(1);
                emitter.onComplete();
            }
        });

        mSaveRecord = observer.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.saveRecordSuccess();
                    }
                });

    }
}
