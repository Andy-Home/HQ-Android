package com.andy.function.main.chart;

import android.view.View;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;
import com.andy.function.main.chart.view.PieChart;

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
    private ChartPresent mPresent;
    @Override
    protected void initView(View view) {
        vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_chart);

        vPieChart = view.findViewById(R.id.chart);

        mPresent = new ChartPresent(this);
        getLifecycle().addObserver(mPresent);
        mPresent.getCatalog(System.currentTimeMillis(), 0, 0);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void displayChart() {

    }
}
