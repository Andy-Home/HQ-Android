package com.andy.function.catalog_edit;

import android.annotation.SuppressLint;
import android.content.Context;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogEditPresent implements CatalogEditContract.Present {
    private Context mContext;
    private CatalogEditContract.View mView;

    CatalogEditPresent(Context context, CatalogEditContract.View view) {
        mContext = context;
        mView = view;
        init();
    }

    private CatalogDao mCatalogDao;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCatalog(int id) {
        mCatalogDao.queryCatalog(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Catalog>() {
                    @Override
                    public void accept(Catalog catalog) throws Exception {
                        mView.displayCatalog(catalog);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void save(final Catalog catalog) {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                mCatalogDao.insert(catalog);
                emitter.onNext(1);
                emitter.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.saveSuccess();
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void delete(final Catalog catalog) {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                mCatalogDao.deleteCatalog(catalog);
                emitter.onNext(1);
                emitter.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.deleteSuccess();
                    }
                });
    }
}
