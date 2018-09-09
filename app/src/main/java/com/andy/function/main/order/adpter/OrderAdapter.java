package com.andy.function.main.order.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.R;
import com.andy.dao.db.entity.RecordContent;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<RecordContent> mData;
    private Context mContext;

    public OrderAdapter(@NotNull List<RecordContent> mData, @NotNull Context context) {
        this.mData = mData;
        this.mContext = context;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_record_type, parent, false);
        return new OrderViewHolder(view);
    }

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy - MM - dd EEE", Locale.CHINA);

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        RecordContent content = mData.get(position);

        if (content.status == 1) {
            holder.time.setText(mFormat.format(new Date(content.time)));
        } else {
            holder.time.setVisibility(View.GONE);
        }
        display(holder, content);
    }

    private void display(OrderViewHolder holder, RecordContent content) {
        if (content.type == 0) {
            holder.type.setText(R.string.record_expenditure);
        } else {
            holder.type.setText(R.string.record_earnings);
        }

        holder.num.setText(String.valueOf(content.num));
        holder.catalog.setText(content.catalogName);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView headerView;
        TextView type;
        TextView catalog;
        TextView num;

        OrderViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            headerView = itemView.findViewById(R.id.head_img);
            type = itemView.findViewById(R.id.type);
            catalog = itemView.findViewById(R.id.catalog);
            num = itemView.findViewById(R.id.num);
        }
    }
}
