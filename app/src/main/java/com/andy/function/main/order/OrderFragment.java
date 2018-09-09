package com.andy.function.main.order;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;
import com.andy.dao.db.entity.RecordContent;
import com.andy.function.main.order.adpter.OrderAdapter;
import com.andy.function.new_record.NewRecordActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2018/8/29.
 * Modify time 2018/8/29
 */
public class OrderFragment extends BaseFragment implements OrderContract.View {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_order;
    }

    private TextView vTitle;
    private FloatingActionButton vNewRecord;

    private RecyclerView vList;

    private OrderPresent mPresent;
    private OrderAdapter mAdapter = null;
    private List<RecordContent> mData = new ArrayList<>();
    @Override
    protected void initView(View view) {
        vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_order);

        vNewRecord = view.findViewById(R.id.fab);

        vList = view.findViewById(R.id.list);
        vList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter(mData, getContext());
        vList.setAdapter(mAdapter);

        mPresent = new OrderPresent(this);
        mPresent.getRecords(System.currentTimeMillis(), 0, 10);
        getLifecycle().addObserver(mPresent);
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

    @Override
    public void displayRecords(List<RecordContent> data) {
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }
}
