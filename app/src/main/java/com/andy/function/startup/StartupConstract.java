package com.andy.function.startup;

import android.app.Activity;
import android.content.Intent;

import com.andy.function.BaseView;

/**
 * Created by Andy on 2018/9/30.
 * Modify time 2018/9/30
 */
public class StartupConstract {

    public interface View extends BaseView {

        void loginSuccess(int userId);

        void needLogin();
    }

    public interface Present {
        void login(Activity activity);

        void setResult(int requestCode, int resultCode, Intent data);
    }
}
