package com.andy.function.startup;

import android.content.Intent;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.login.LoginActivity;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

/**
 * Created by Andy on 2018/9/30.
 * Modify time 2018/9/30
 */
public class StartupPageActivity extends BaseActivity implements StartupConstract.View {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_startup_page;
    }


    private StartupConstract.Present mPresent;

    @Override
    protected void initView() {
        mPresent = new StartupPresent(this, this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent.login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresent.setResult(requestCode, resultCode, data);
    }

    @Override
    public void loginSuccess(int userId) {
        MainActivity.start(StartupPageActivity.this);
    }

    @Override
    public void needLogin() {
        LoginActivity.start(StartupPageActivity.this);
    }

    @Override
    public void onError(String msg) {
        ToastUtils.shortShow(StartupPageActivity.this, msg);
    }
}
