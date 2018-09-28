package com.andy.function.login;

import android.app.Activity;
import android.content.Intent;

import com.andy.function.BaseView;

/**
 * Created by Andy on 2018/9/28.
 * Modify time 2018/9/28
 */
public class LoginContract {

    public interface View extends BaseView {
        void loginSuccess(int userId);
    }

    public interface Present {
        void QQLogin(Activity activity);

        void setResult(int requestCode, int resultCode, Intent data);
    }
}
