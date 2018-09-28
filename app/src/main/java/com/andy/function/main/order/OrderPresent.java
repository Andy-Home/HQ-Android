package com.andy.function.main.order;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.RecordContent;

import java.util.List;

public class OrderPresent implements OrderContract.Present, LifecycleObserver {
    private OrderContract.View mView;

    public OrderPresent(OrderContract.View view) {
        mView = view;
        init();
    }

    private DaoManager mDaoManager;

    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initRecordService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mView = null;
    }

    @Override
    public void getRecords(long startTime, long endTime, int num) {
        mDaoManager.mRecordService.getRecordList(startTime, new BaseListener<List<RecordContent>>() {

            @Override
            public void onSuccess(List<RecordContent> recordContents) {
                if (mView != null) {
                    mView.displayRecords((recordContents));
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
