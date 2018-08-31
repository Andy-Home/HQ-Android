package com.andy.function.catalog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.R;
import com.andy.dao.db.entity.Catalog;

import java.util.ArrayList;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {
    private Context mContext;
    private ArrayList<Catalog> mData;

    public CatalogAdapter(Context context, ArrayList<Catalog> list){
        mContext = context;
        mData = list;
    }

    public static int DEFAULT = 0;
    public static int EDIT = 1;
    private int status;
    public void changeStatus(int status){
        this.status = status;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_catalog, parent,false);
        return new CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, final int position) {
        if(status == DEFAULT){
            holder.rightImg.setVisibility(View.VISIBLE);
            holder.editImg.setVisibility(View.GONE);
        }else if(status == EDIT){
            holder.rightImg.setVisibility(View.GONE);
            holder.editImg.setVisibility(View.VISIBLE);
        }

        holder.text.setText(mData.get(position).name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(mData.get(position), position, status);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CatalogViewHolder extends RecyclerView.ViewHolder{

        ImageView rightImg,editImg;
        TextView text;

        CatalogViewHolder(View itemView) {
            super(itemView);

            rightImg = itemView.findViewById(R.id.right_icon);
            editImg = itemView.findViewById(R.id.edit);

            text = itemView.findViewById(R.id.text);
        }
    }


    public interface OnItemClickListener{

        void onItemClick(Catalog item, int position, int status);
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
