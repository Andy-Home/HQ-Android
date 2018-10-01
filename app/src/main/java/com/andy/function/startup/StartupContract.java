package com.andy.function.startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.andy.function.BaseView;

/**
 * Created by Andy on 2018/9/30.
 * Modify time 2018/9/30
 */
public class StartupContract {

    public interface View extends BaseView {

        void loginSuccess(int userId);

        void needLogin();

        void displayVersion(String version);
    }

    public interface Present {
        void login(Activity activity);

        void setResult(int requestCode, int resultCode, Intent data);

        void getVersion(Context context);
    }
}
