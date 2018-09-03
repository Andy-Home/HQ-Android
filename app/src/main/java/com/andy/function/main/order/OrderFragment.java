package com.andy.function.main.order;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;
import com.andy.function.new_record.NewRecordActivity;

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
    private FloatingActionButton vNewRecord;

    @Override
    protected void initView(View view) {
        vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_order);

        vNewRecord = view.findViewById(R.id.fab);
    }

    @Override
    protected void setListener() {
        vNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewRecordActivity.start(getActivity());
            }
        });
    }
}
