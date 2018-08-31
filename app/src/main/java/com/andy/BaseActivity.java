package com.andy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(getContentViewId());
        initView();
        setListener();
    }

    protected abstract int getContentViewId();

    protected Intent dataIntent;

    protected void getData() {
        dataIntent = getIntent();
    }


    protected abstract void initView();

    protected abstract void setListener();
}
