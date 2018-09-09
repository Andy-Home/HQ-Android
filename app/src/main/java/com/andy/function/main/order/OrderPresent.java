package com.andy.function.main.order;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.db.DBManage;
import com.andy.dao.db.RecordDao;
import com.andy.dao.db.entity.RecordContent;

import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderPresent implements OrderContract.Present, LifecycleObserver {
    private OrderContract.View mView;

    public OrderPresent(OrderContract.View view) {
        mView = view;
        init();
    }

    private RecordDao mRecordDao;

    private void init() {
        mRecordDao = DBManage.getInstance().getRecordDao();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mGetRecords != null && !mGetRecords.isDisposed()) {
            mGetRecords.dispose();
        }
    }

    private Disposable mGetRecords = null;

    @Override
    public void getRecords(long startTime, long endTime, final int num) {
        mGetRecords = mRecordDao.queryRecordContents(startTime, endTime, num)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecordContent>>() {
                    @Override
                    public void accept(List<RecordContent> records) {
                        Calendar calendar = null;

                        for (RecordContent content : records) {
                            Calendar temp = Calendar.getInstance();
                            temp.setTimeInMillis(content.time);
                            if (calendar == null || calendar.get(Calendar.DAY_OF_MONTH) != temp.get(Calendar.DAY_OF_MONTH)) {
                                calendar = temp;
                                content.status = 1;
                            }
                        }
                        mView.displayRecords(records);
                    }
                });
    }
}
