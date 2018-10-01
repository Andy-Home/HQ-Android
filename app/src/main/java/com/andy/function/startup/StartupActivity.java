package com.andy.function.startup;

import android.content.Intent;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.login.LoginActivity;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andy on 2018/9/30.
 * Modify time 2018/9/30
 */
public class StartupActivity extends BaseActivity implements StartupContract.View {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_startup_page;
    }


    private StartupContract.Present mPresent;

    private TextView vVersion;
    @Override
    protected void initView() {
        vVersion = findViewById(R.id.version);

        mPresent = new StartupPresent(this, this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent.login(this);
        mPresent.getVersion(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresent.setResult(requestCode, resultCode, data);
    }

    @Override
    public void loginSuccess(int userId) {
        MainActivity.start(StartupActivity.this);
    }

    @Override
    public void needLogin() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LoginActivity.start(StartupActivity.this);
            }
        }, 2000);
    }

    @Override
    public void displayVersion(String version) {
        vVersion.setText(String.format("Version：%s", version));
    }

    @Override
    public void onError(String msg) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LoginActivity.start(StartupActivity.this);
            }
        }, 2000);
        ToastUtils.shortShow(StartupActivity.this, msg);
    }
}
