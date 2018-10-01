package com.andy.function.startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.andy.HQApplication;
import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.utils.SharedPreferencesUtils;
import com.andy.utils.ToastUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Andy on 2018/9/30.
 * Modify time 2018/9/30
 */
public class StartupPresent implements StartupContract.Present {
    private Context mContext;
    private StartupContract.View mView;

    StartupPresent(StartupContract.View view, Context context) {
        mContext = context;
        mView = view;
        init();
    }

    private DaoManager mDaoManager;
    private SharedPreferencesUtils mSharedPreferencesUtils;

    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initUserService();
        mSharedPreferencesUtils = SharedPreferencesUtils.getInstance();
    }

    private Tencent mTencent = HQApplication.mTencent;
    private IUiListener mListener = null;

    private String mQQToken;
    private String mQQExpires;
    private String mQQOpenId;

    @Override
    public void login(Activity activity) {
        String channel = mSharedPreferencesUtils.getLoginChannel();
        if (channel.equals("QQ")) {
            Map<String, String> map = mSharedPreferencesUtils.getQQLoginStates();

            mQQToken = map.get("token");
            mQQExpires = map.get("expires");
            mQQOpenId = map.get("openId");

            if (!(mQQToken.equals("") || mQQExpires.equals("") || mQQOpenId.equals(""))) {
                mTencent.setAccessToken(mQQToken, mQQExpires);
                mTencent.setOpenId(mQQOpenId);
            }
            QQLogin();
        } else {
            mView.needLogin();
        }
    }


    private void QQLogin() {
        mListener = new IUiListener() {
            @Override
            public void onComplete(Object response) {
                if (null == response) {
                    ToastUtils.shortShow(mContext, "返回为空,登录失败");
                    return;
                }

                JSONObject jsonResponse = (JSONObject) response;
                if (jsonResponse.length() == 0) {
                    ToastUtils.shortShow(mContext, "返回为空,登录失败");
                    return;
                }

                try {
                    if (jsonResponse.getString("ret").equals("0")) {
                        mDaoManager.mUserService.QQLogin(mQQOpenId, mQQToken, new BaseListener<Integer>() {

                            @Override
                            public void onSuccess(Integer o) {

                                mTencent.setAccessToken(mQQToken, mQQExpires);
                                mTencent.setOpenId(mQQOpenId);

                                mSharedPreferencesUtils.putLoginChannel("QQ");
                                mSharedPreferencesUtils.putQQLoginStates(mQQToken, mQQExpires, mQQOpenId);
                                mView.loginSuccess(o);
                            }

                            @Override
                            public void onError(String msg) {
                                mView.onError(msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    mView.onError(e.getMessage());
                }
            }

            @Override
            public void onError(UiError uiError) {
                ToastUtils.shortShow(mContext, uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                ToastUtils.shortShow(mContext, "已取消QQ登录");
            }
        };

        if (mTencent.isSessionValid()) {
            mTencent.checkLogin(mListener);
        } else {
            mView.needLogin();
        }
    }

    @Override
    public void setResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    @Override
    public void getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            mView.displayVersion(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
