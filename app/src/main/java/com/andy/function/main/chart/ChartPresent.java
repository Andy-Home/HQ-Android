package com.andy.function.main.chart;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.RecordStatistics;

import java.util.Calendar;
import java.util.List;

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

    private DaoManager mDaoManager;

    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initCatalogService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mView = null;
    }

    @Override
    public void getCatalog(int id) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int month = start.get(Calendar.MONTH) + 1;
        int year = start.get(Calendar.YEAR);
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }

        start.set(year, month, 1, 0, 0, 0);

        if (month == 12) {
            year++;
            month = 1;
        } else {
            month++;
        }
        end.set(year, month, 1, 0, 0, 0);

        long startTime = start.getTimeInMillis();
        long endTime = end.getTimeInMillis();
        mDaoManager.mCatalogService.getCatalog(startTime, endTime, id, new BaseListener<List<RecordStatistics>>() {

            @Override
            public void onSuccess(List<RecordStatistics> recordStatistics) {
                if (mView != null) {
                    mView.displayChart(recordStatistics);
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
