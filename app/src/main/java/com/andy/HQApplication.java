package com.andy;

import android.app.Application;

import com.Constant;
import com.andy.dao.db.DBManage;
import com.andy.utils.DPValueUtil;
import com.andy.utils.SharedPreferencesUtils;
import com.tencent.tauth.Tencent;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class HQApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    public static Tencent mTencent;

    private void init() {
        DBManage.getInstance().init(this);
        DPValueUtil.getInstance().init(this);

        SharedPreferencesUtils.getInstance().init(this);
        mTencent = Tencent.createInstance(Constant.QQ_APPID, this);
    }
}
