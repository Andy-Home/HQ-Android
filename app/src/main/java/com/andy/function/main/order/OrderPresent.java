package com.andy.function.main.order;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.RecordContent;

import java.util.Calendar;
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

    private int mCurrentMonth, mCurrentYear;
    @Override
    public void getRecords(int month, int year) {
        mCurrentMonth = month;
        mCurrentYear = year;
        final int finalYear = year;
        final int finalMonth = month;

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
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

        mDaoManager.mRecordService.getRecordList(startTime, endTime, new BaseListener<List<RecordContent>>() {
            @Override
            public void onSuccess(List<RecordContent> recordContents) {
                if (mView != null && finalMonth == mCurrentMonth && finalYear == mCurrentYear) {
                    Calendar calendar = null;

                    for (RecordContent content : recordContents) {
                        Calendar temp = Calendar.getInstance();
                        temp.setTimeInMillis(content.time);
                        if (calendar == null || calendar.get(Calendar.DAY_OF_MONTH) != temp.get(Calendar.DAY_OF_MONTH)) {
                            calendar = temp;
                            content.status = 1;
                        }
                    }
                    mView.displayRecords(recordContents);
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
