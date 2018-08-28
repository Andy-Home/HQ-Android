package com.andy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andy on 2018/7/10.
 * Modify time 2018/7/10
 */
public abstract class BaseFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(getContentViewId(), container, false);
        initView(view);
        setListener();
        return view;
    }

    protected abstract int getContentViewId();

    protected abstract void initView(View view);

    protected abstract void setListener();
}
