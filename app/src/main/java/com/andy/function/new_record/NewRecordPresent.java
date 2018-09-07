package com.andy.function.new_record;

import android.annotation.SuppressLint;
import android.view.View;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;
import com.andy.function.new_record.entity.CatalogItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/7.
 * Modify time 2018/9/7
 */
public class NewRecordPresent implements NewRecordContract.Present {
    private NewRecordContract.Views mView;

    public NewRecordPresent(NewRecordContract.Views view) {
        mView = view;
        init();
    }

    private CatalogDao mCatalogDao;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCatalog(final View view, int id) {
        mCatalogDao.queryCatalogList(id)
                .subscribeOn(Schedulers.io())
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
                            items.add(item);
                        }
                        mView.displayCatalogWindow(view, items);
                    }
                });
    }
}
