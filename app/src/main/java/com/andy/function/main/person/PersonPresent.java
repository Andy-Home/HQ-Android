package com.andy.function.main.person;

import android.content.Context;

import com.andy.HQApplication;
import com.andy.utils.SharedPreferencesUtils;

/**
 * Created by Andy on 2018/9/29.
 * Modify time 2018/9/29
 */
public class PersonPresent implements PersonContract.Present {
    private Context mContext;
    private PersonContract.View mView;

    public PersonPresent(PersonContract.View view, Context context) {
        mContext = context;
        mView = view;
        init();
    }

    private SharedPreferencesUtils mSharedPreferencesUtils;

    private void init() {
        mSharedPreferencesUtils = SharedPreferencesUtils.getInstance();
    }

    @Override
    public void logout() {
        HQApplication.mTencent.logout(mContext);
        mSharedPreferencesUtils.putLoginChannel("");
        mSharedPreferencesUtils.putUserId(0);
        mSharedPreferencesUtils.putQQLoginStates("", "", "");
        mView.logoutSuccess();
    }
}
