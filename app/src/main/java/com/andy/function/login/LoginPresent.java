package com.andy.function.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.andy.HQApplication;
import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.utils.SharedPreferencesUtils;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Andy on 2018/9/28.
 * Modify time 2018/9/28
 */
public class LoginPresent implements LoginContract.Present {

    private Context mContext;
    private LoginContract.View mView;

    LoginPresent(LoginContract.View view, Context context) {
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
    public void checkLogin(Activity activity) {
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
            QQLogin(activity);
        }
    }

    private boolean mQQGrant = true;
    @Override
    public void QQLogin(Activity activity) {
        mListener = new IUiListener() {
            @Override
            public void onComplete(Object response) {
                if (null == response) {
                    mView.onError("返回为空,登录失败");
                    return;
                }

                JSONObject jsonResponse = (JSONObject) response;
                if (jsonResponse.length() == 0) {
                    mView.onError("返回为空,登录失败");
                    return;
                }

                try {
                    if (jsonResponse.getString("ret").equals("0")) {
                        QQLogin(jsonResponse);
                    }
                } catch (JSONException e) {
                    mView.onError(e.getMessage());
                }
            }

            @Override
            public void onError(UiError uiError) {
                mView.onError(uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                mView.onError("已取消QQ登录");
            }
        };

        if (!mTencent.isSessionValid()) {
            mQQGrant = true;
            mTencent.login(activity, "all", mListener);
        } else {
            mQQGrant = false;
            mTencent.checkLogin(mListener);
        }
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    private void QQLogin(final JSONObject jsonObject) {
        if (mQQGrant) {
            try {
                mQQToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                mQQExpires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                mQQOpenId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            } catch (Exception e) {
                mView.onError(e.getMessage());
            }
        }

        if (!TextUtils.isEmpty(mQQToken) && !TextUtils.isEmpty(mQQExpires)
                && !TextUtils.isEmpty(mQQOpenId)) {
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
    }
}
