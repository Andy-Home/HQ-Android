package com.andy.function.main.person;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.andy.HQApplication;
import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.dao.db.entity.User;
import com.andy.utils.SharedPreferencesUtils;

/**
 * Created by Andy on 2018/9/29.
 * Modify time 2018/9/29
 */
public class PersonPresent implements PersonContract.Present, LifecycleObserver {
    private Context mContext;
    private PersonContract.View mView;

    public PersonPresent(PersonContract.View view, Context context) {
        mContext = context;
        mView = view;
        init();
    }

    private SharedPreferencesUtils mSharedPreferencesUtils;
    private DaoManager mDaoManager;
    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initUserService();
        mSharedPreferencesUtils = SharedPreferencesUtils.getInstance();
    }

    @Override
    public void logout() {
        HQApplication.mTencent.logout(mContext);
        mSharedPreferencesUtils.putLoginChannel("");
        mSharedPreferencesUtils.putUserId(0);
        mSharedPreferencesUtils.putQQLoginStates("", "", "");
        mSharedPreferencesUtils.putCatalogModifyTime(0);
        mSharedPreferencesUtils.syncUpdateTime(0);
        mSharedPreferencesUtils.syncStartTime(0);
        mSharedPreferencesUtils.syncEndTime(0);
        mSharedPreferencesUtils.putHomeUsers("");
        mView.logoutSuccess();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        mDaoManager.mUserService.getUserInfo(new BaseListener<User>() {
            @Override
            public void onSuccess(User user) {
                if (mView != null) {
                    mView.displayUserInfo(user);
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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop() {
        mView = null;
    }
}
