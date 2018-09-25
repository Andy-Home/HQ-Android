package com.andy.function.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.main.MainActivity;

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
        return R.layout.activity_login_new;
    }

    private ImageView vQQLogin;
    @Override
    protected void initView() {
        vQQLogin = findViewById(R.id.qq);
    }

    @Override
    protected void setListener() {
        vQQLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(LoginActivity.this);
            }
        });
    }
}
