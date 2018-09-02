package com.andy.function.catalog;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.dao.db.entity.Catalog;
import com.andy.function.catalog.adapter.CatalogAdapter;
import com.andy.function.catalog_edit.CatalogEditActivity;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 账务目录，记录账务关系
 * <p>
 * Created by Andy on 2018/8/30.
 * Modify time 2018/8/30
 */
public class CatalogActivity extends BaseActivity implements CatalogContract.View {

    public static void start(Context context, int parentId, String parentName) {
        Intent intent = new Intent(context, CatalogActivity.class);
        intent.putExtra("parentId", parentId);
        intent.putExtra("parentName", parentName);

        context.startActivity(intent);
    }

    private int mParentId;
    private String mParentName;

    private CatalogPresent mPresent;

    @Override
    protected void getData() {
        super.getData();

        mParentId = dataIntent.getIntExtra("parentId", -1);
        if (mParentId == -1) {
            ToastUtils.shortShow(this, "error");
            finish();
        }

        mParentName = dataIntent.getStringExtra("parentName");

        mPresent = new CatalogPresent(this, this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_catalog;
    }

    private ImageView vBack;
    private RecyclerView vRecyclerView = null;

    private FloatingActionButton vToolbox, vEdit, vNewCatalog;

    private CatalogAdapter mAdapter = null;
    private ArrayList<Catalog> mData = new ArrayList<>();

    @Override
    protected void initView() {
        TextView vTitle = findViewById(R.id.title);
        if (mParentId == 0) {
            vTitle.setText(R.string.catalog_title);
        } else {
            vTitle.setText(mParentName);
        }

        vBack = findViewById(R.id.back);
        vBack.setVisibility(View.VISIBLE);

        vRecyclerView = findViewById(R.id.list);

        vToolbox = findViewById(R.id.toolbox);
        vEdit = findViewById(R.id.edit);
        vNewCatalog = findViewById(R.id.new_catalog);

        vRecyclerView = findViewById(R.id.list);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CatalogAdapter(this, mData);
        vRecyclerView.setAdapter(mAdapter);

        mPresent.getCatalogs(mParentId, MainActivity.getUserId());
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
                if (mOpenToolbox) {
                    mOpenToolbox = false;
                    CatalogAnimator.funBackAnimator(vToolbox, vNewCatalog, 1);
                    CatalogAnimator.funBackAnimator(vToolbox, vEdit, 2);
                } else {
                    mOpenToolbox = true;
                    CatalogAnimator.funPopAnimator(vToolbox, vNewCatalog, 1);
                    CatalogAnimator.funPopAnimator(vToolbox, vEdit, 2);
                }
            }
        });

        vNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.NEW, mParentId, mParentName, 0);
            }
        });

        vEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vToolbox.performClick();
                if (mAdapter.currentStatus() == CatalogAdapter.DEFAULT) {
                    mAdapter.changeStatus(CatalogAdapter.EDIT);
                    vEdit.setImageResource(R.mipmap.right_white);
                } else if (mAdapter.currentStatus() == CatalogAdapter.EDIT) {
                    mAdapter.changeStatus(CatalogAdapter.DEFAULT);
                    vEdit.setImageResource(R.mipmap.edit_white);
                }

            }
        });

        mAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Catalog item, int position, int status) {
                if (status == CatalogAdapter.DEFAULT) {

                    CatalogActivity.start(CatalogActivity.this, item.id, item.name);

                } else if (status == CatalogAdapter.EDIT) {

                    CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.EDIT, mParentId, mParentName, item.id);

                }
            }
        });
    }

    @Override
    public void displayCatalogs(ArrayList<Catalog> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
