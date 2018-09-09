package com.andy.function.new_record.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andy.R;
import com.andy.function.new_record.entity.CatalogItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2018/9/7.
 * Modify time 2018/9/7
 */
public class CatalogPopupWindow extends PopupWindow {
    private Context mContext;
    private View mView;

    public CatalogPopupWindow(Context context, View view) {
        super(context);
        mContext = context;
        mView = view;
        init();
    }

    private RecyclerView vList;
    private CatalogAdapter mAdapter;

    private void init() {
        setWidth(mView.getWidth());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(0x00000000));
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_list, null);
        vList = view.findViewById(R.id.list);
        vList.setLayoutManager(new LinearLayoutManager(mContext));

        setContentView(view);
        setFocusable(true);
        setOutsideTouchable(false);

        mAdapter = new CatalogAdapter();
        vList.setAdapter(mAdapter);
    }

    private List<CatalogItem> mData = new ArrayList<>();

    public void setData(List<CatalogItem> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public void show() {
        this.showAsDropDown(mView);
    }

    public interface onItemClickListener {
        void onItemClick(int position, CatalogItem item);
    }

    private onItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.pop_item_catalog, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.text.setText(mData.get(position).text);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, mData.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView text;

            ViewHolder(View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.text);
            }
        }
    }
}
