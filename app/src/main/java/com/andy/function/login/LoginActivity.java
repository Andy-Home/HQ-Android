package com.andy.function.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {
    private final String TAG = LoginActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_new;
    }

    private ImageView vQQLogin;

    @Override
    protected void initView() {
        vQQLogin = findViewById(R.id.qq);
    }

    private LoginContract.Present mPresent;

    @Override
    protected void onResume() {
        super.onResume();
        mPresent = new LoginPresent(this, this);
    }

    @Override
    protected void setListener() {
        vQQLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.QQLogin(LoginActivity.this);
            }
        });
    }

    @Override
    public void loginSuccess(int userId) {
        MainActivity.start(LoginActivity.this);
    }

    @Override
    public void onError(String msg) {
        ToastUtils.shortShow(LoginActivity.this, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        mPresent.setResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
