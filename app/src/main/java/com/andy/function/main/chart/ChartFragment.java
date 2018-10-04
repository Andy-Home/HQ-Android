package com.andy.function.main.chart;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;
import com.andy.dao.db.entity.RecordStatistics;
import com.andy.function.main.chart.adapter.ChartAdapter;
import com.andy.function.main.chart.entity.PieContent;
import com.andy.function.main.chart.view.PieChart;
import com.andy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2018/8/29.
 * Modify time 2018/8/29
 */
public class ChartFragment extends BaseFragment implements ChartContract.View {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_chart;
    }

    private TextView vTitle;

    private PieChart vPieChart;
    private RecyclerView vList;

    private ChartPresent mPresent;
    private ChartAdapter mAdapter;

    private ConstraintLayout vNoChart;
    private ConstraintLayout vMain;
    @Override
    protected void initView(View view) {
        vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_chart);

        vPieChart = view.findViewById(R.id.chart);

        vNoChart = view.findViewById(R.id.no_chart);
        vNoChart.setVisibility(View.VISIBLE);

        vList = view.findViewById(R.id.list);
        vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ChartAdapter(getActivity(), mData, mTotal);
        vList.setAdapter(mAdapter);

        vMain = view.findViewById(R.id.constraintLayout);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresent = new ChartPresent(this);
        getLifecycle().addObserver(mPresent);
        mPresent.getCatalog(System.currentTimeMillis(), 0, 0);
    }

    private double mTotal = 0.0;
    private List<RecordStatistics> mData = new ArrayList<>();
    @Override
    public void displayChart(List<RecordStatistics> data) {
        mData.clear();
        mData.addAll(data);

        if (mData.size() == 0) {
            vMain.setVisibility(View.GONE);
            vNoChart.setVisibility(View.VISIBLE);
            return;
        } else {
            vMain.setVisibility(View.VISIBLE);
            vNoChart.setVisibility(View.GONE);
        }

        mTotal = 0.0;
        PieContent[] pies = new PieContent[data.size()];
        for (int i = 0; i < data.size(); i++) {
            PieContent content = new PieContent();
            RecordStatistics recordStatistics = data.get(i);
            mTotal += recordStatistics.num;
            content.setNum(recordStatistics.num);
            content.setContent(recordStatistics.catalogName);
            pies[i] = content;
        }
        mAdapter.setTotal(mTotal);
        mAdapter.notifyDataSetChanged();
        vPieChart.setData(pies);

    }

    @Override
    public void onError(String msg) {
        ToastUtils.shortShow(getContext(), msg);
    }
}
