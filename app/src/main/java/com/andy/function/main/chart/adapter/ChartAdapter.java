package com.andy.function.main.chart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.R;
import com.andy.dao.db.entity.RecordStatistics;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Andy on 2018/9/12.
 * Modify time 2018/9/12
 */
public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ChartViewHolder> {
    private Context mContext;
    private List<RecordStatistics> mData;

    private double mTotal;

    public ChartAdapter(Context context, List<RecordStatistics> data, double total) {
        mData = data;
        mContext = context;
        mTotal = total;
    }

    public void setTotal(double total) {
        mTotal = total;
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chart_data, parent, false);
        return new ChartViewHolder(view);
    }

    private int[] mColor = {R.color.color11, R.color.color12, R.color.color13, R.color.color14,
            R.color.color15, R.color.color16, R.color.color17, R.color.color18};
    DecimalFormat mFormat = new DecimalFormat("##0.00");

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder holder, int position) {
        RecordStatistics recordStatistics = mData.get(position);

        holder.catalogName.setTextColor(mContext.getResources().getColor(mColor[position % 8]));
        holder.catalogName.setText(recordStatistics.catalogName);
        holder.num.setText(String.valueOf(recordStatistics.num));

        float percent = (float) (recordStatistics.num / mTotal);
        holder.percent.setText(String.format("%s%%", mFormat.format(percent * 100)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ChartViewHolder extends RecyclerView.ViewHolder {
        TextView catalogName;
        TextView num;
        TextView percent;

        ChartViewHolder(View itemView) {
            super(itemView);

            catalogName = itemView.findViewById(R.id.catalog_name);
            num = itemView.findViewById(R.id.num);
            percent = itemView.findViewById(R.id.percent);
        }
    }
}
