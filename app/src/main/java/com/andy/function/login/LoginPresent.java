package com.andy.function.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.Constant;
import com.andy.dao.BaseListener;
import com.andy.dao.DaoManager;
import com.andy.utils.ToastUtils;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

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

    private void init() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.initUserService();
    }

    private Tencent mTencent = null;
    private IUiListener mListener = null;

    @Override
    public void QQLogin(Activity activity) {
        mTencent = Tencent.createInstance(Constant.QQ_APPID, mContext);

        if (!mTencent.isSessionValid()) {
            mTencent.logout(mContext);
            mTencent.login(activity, "all", mListener = new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    ToastUtils.shortShow(mContext, "授权成功");

                    if (null == response) {
                        ToastUtils.shortShow(mContext, "返回为空,登录失败");
                        return;
                    }

                    JSONObject jsonResponse = (JSONObject) response;
                    if (jsonResponse.length() == 0) {
                        ToastUtils.shortShow(mContext, "返回为空,登录失败");
                        return;
                    }

                    QQLogin(jsonResponse);
                }

                @Override
                public void onError(UiError uiError) {
                    ToastUtils.shortShow(mContext, uiError.errorDetail);
                }

                @Override
                public void onCancel() {
                    ToastUtils.shortShow(mContext, "已取消QQ登录");
                }
            });
        }
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    private void QQLogin(JSONObject jsonObject) {
        try {
            final String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            final String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            final String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mDaoManager.mUserService.QQLogin(openId, token, new BaseListener<Object>() {

                    @Override
                    public void onSuccess(Object o) {
                        mTencent.setAccessToken(token, expires);
                        mTencent.setOpenId(openId);
                        mView.loginSuccess(0);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.onError(msg);
                    }
                });

            }
        } catch (Exception e) {
            mView.onError(e.getMessage());
        }
    }
}
