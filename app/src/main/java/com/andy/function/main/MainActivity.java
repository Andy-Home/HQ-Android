package com.andy.function.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class MainActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }
}
