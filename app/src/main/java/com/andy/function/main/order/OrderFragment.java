package com.andy.function.main.order;

import android.view.View;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;

/**
 * Created by Andy on 2018/8/29.
 * Modify time 2018/8/29
 */
public class OrderFragment extends BaseFragment {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_order;
    }

    private TextView vTitle;

    @Override
    protected void initView(View view) {
        vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_order);
    }

    @Override
    protected void setListener() {

    }
}
