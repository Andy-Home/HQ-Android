package com.andy.dao;

import android.util.Log;

import com.andy.dao.db.DBManage;
import com.andy.dao.db.UserDao;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.Response;
import com.andy.dao.net.UserRequest;
import com.andy.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/28.
 * Modify time 2018/9/28
 */
public class UserService {
    private final static String TAG = UserService.class.getSimpleName();

    private UserService() {
        init();
    }

    protected static UserService getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        final static UserService mInstance = new UserService();
    }

    private UserDao mUserDao;
    private UserRequest mUserRequest;
    private SharedPreferencesUtils utils = null;

    private void init() {
        mUserDao = DBManage.getInstance().getUserDao();
        mUserRequest = NetRequestManager.getInstance().getUserRequest();
        utils = SharedPreferencesUtils.getInstance();
    }

    public void QQLogin(String openId, String accessToken, final BaseListener<Integer> listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", "QQ");
            jsonObject.put("openId", openId);
            jsonObject.put("accessToken", accessToken);
        } catch (JSONException e) {
            listener.onError("data can not cast to json");
            return;
        }
        Log.d(TAG, "net qqlogin");
        mUserRequest.login(jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getCode() == 0) {
                            int userId = (int) response.getResult();
                            utils.putUserId(userId);
                            listener.onSuccess(userId);
                            onComplete();
                        } else {
                            onError(new Throwable(response.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.getMessage());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
