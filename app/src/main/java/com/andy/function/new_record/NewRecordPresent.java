package com.andy.function.new_record;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.Record;
import com.andy.function.new_record.entity.CatalogItem;

import java.util.ArrayList;
import java.util.List;

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

    private DaoManager mDaoManager;
    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initRecordService();
        mDaoManager.initCatalogService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mView = null;
    }


    @Override
    public void getCatalog(final View view, int id) {
        mDaoManager.mCatalogService.getCatalogList(id, new BaseListener<ArrayList<Catalog>>() {
            @Override
            public void onSuccess(ArrayList<Catalog> data) {
                if (mView == null) {
                    return;
                }

                List<CatalogItem> items = new ArrayList<>();
                for (Catalog catalog : data) {
                    CatalogItem item = new CatalogItem();
                    item.text = catalog.name;
                    item.id = catalog.id;
                    item.parentId = catalog.parentId;
                    item.type = catalog.style;
                    items.add(item);
                }

                mView.showCatalogWindow(view, items);
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
    public void saveRecord(final Record record) {
        mDaoManager.mRecordService.saveRecord(record, new BaseListener<Object>() {

            @Override
            public void onSuccess(Object o) {
                if (mView != null) {
                    mView.saveRecordSuccess();
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
