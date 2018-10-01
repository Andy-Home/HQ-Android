package com.andy.function.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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

    private AlertDialog vLoginDialog;
    @Override
    protected void initView() {
        vQQLogin = findViewById(R.id.qq);

        View view = LayoutInflater.from(this)
                .inflate(R.layout.anim_login, null);

        vLoginDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        vLoginDialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(0));
        vLoginDialog.setCancelable(false);
    }

    private LoginContract.Present mPresent;

    @Override
    protected void onResume() {
        super.onResume();
        mPresent = new LoginPresent(this, this);
        mPresent.checkLogin(this);
    }

    @Override
    protected void setListener() {
        vQQLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.QQLogin(LoginActivity.this);
                vLoginDialog.show();
            }
        });
    }

    @Override
    public void loginSuccess(int userId) {
        vLoginDialog.cancel();
        MainActivity.start(LoginActivity.this);
    }

    @Override
    public void onError(String msg) {
        vLoginDialog.cancel();
        ToastUtils.shortShow(LoginActivity.this, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        mPresent.setResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
    }
}
