package com.andy.function.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class LoginActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    private EditText vUser, vPassword;
    private Button vLogin;
    private TextView vRegister, vFindPassword;

    @Override
    protected void initView() {
        vUser = findViewById(R.id.user);
        vPassword = findViewById(R.id.password);
        vPassword.setText("");

        vLogin = findViewById(R.id.login);

        vRegister = findViewById(R.id.register);
        vFindPassword = findViewById(R.id.find_password);
    }

    @Override
    protected void setListener() {
        vLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(LoginActivity.this);
            }
        });

        vRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.shortShow(LoginActivity.this.getBaseContext(), "该功能暂未开放");
            }
        });

        vFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.shortShow(LoginActivity.this.getBaseContext(), "该功能暂未开放");
            }
        });
    }
}
