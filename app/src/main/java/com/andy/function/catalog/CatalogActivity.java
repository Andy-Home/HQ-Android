package com.andy.function.catalog;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.catalog_edit.CatalogEditActivity;

/**
 * 账务目录，记录账务关系
 *
 * Created by Andy on 2018/8/30.
 * Modify time 2018/8/30
 */
public class CatalogActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,CatalogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_catalog;
    }

    private ImageView vBack;
    private RecyclerView vRecyclerView;

    private FloatingActionButton vToolbox, vEdit, vNewCatalog;
    @Override
    protected void initView() {
        TextView vTitle = findViewById(R.id.title);
        vTitle.setText(R.string.catalog_title);

        vBack = findViewById(R.id.back);
        vBack.setVisibility(View.VISIBLE);

        vRecyclerView = findViewById(R.id.list);

        vToolbox = findViewById(R.id.toolbox);
        vEdit = findViewById(R.id.edit);
        vNewCatalog = findViewById(R.id.new_catalog);
    }

    private boolean mOpenToolbox = false;
    @Override
    protected void setListener() {
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vToolbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOpenToolbox){
                    mOpenToolbox = false;
                    CatalogAnimator.funBackAnimator(vToolbox, vNewCatalog, 1);
                    CatalogAnimator.funBackAnimator(vToolbox, vEdit, 2);
                }else {
                    mOpenToolbox = true;
                    CatalogAnimator.funPopAnimator(vToolbox,vNewCatalog,1);
                    CatalogAnimator.funPopAnimator(vToolbox,vEdit,2);
                }
            }
        });

        vNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.NEW);
            }
        });

        vEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.EDIT);
            }
        });
    }
}
