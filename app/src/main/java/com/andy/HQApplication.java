package com.andy;

import android.app.Application;

import com.andy.dao.db.DBManage;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class HQApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        DBManage.getInstance().init(getApplicationContext());
    }
}
