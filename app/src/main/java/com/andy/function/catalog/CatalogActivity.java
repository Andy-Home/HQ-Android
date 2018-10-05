package com.andy.function.catalog;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_catalog;
    }

    private ImageView vBack;
    private RecyclerView vRecyclerView = null;

    private FloatingActionButton vNewCatalog;

    private CatalogAdapter mAdapter = null;
    private ArrayList<Catalog> mData = new ArrayList<>();

    private ConstraintLayout vNoData;
    private SwipeRefreshLayout vRefresh;
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

        vNewCatalog = findViewById(R.id.new_catalog);

        vNoData = findViewById(R.id.no_catalog);

        vRefresh = findViewById(R.id.refresh);
        vRefresh.setColorSchemeResources(R.color.mainColor);

        vRecyclerView = findViewById(R.id.list);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CatalogAdapter(this, mData);
        vRecyclerView.setAdapter(mAdapter);
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

        vNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.NEW, mParentId, mParentName, 0);
            }
        });

        mAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Catalog item, int position, int status) {
                CatalogEditActivity.start(CatalogActivity.this, CatalogEditActivity.EDIT, mParentId, mParentName, item.id);
            }
        });

        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.getCatalogs(mParentId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent = new CatalogPresent(this, this);
        getLifecycle().addObserver(mPresent);
        mPresent.getCatalogs(mParentId);
        vRefresh.setRefreshing(true);
    }

    @Override
    public void displayCatalogs(ArrayList<Catalog> list) {
        vRefresh.setRefreshing(false);
        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.size() == 0) {
            vNoData.setVisibility(View.VISIBLE);
        } else {
            vNoData.setVisibility(View.GONE);
        }

        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        ToastUtils.shortShow(this, msg);
    }
}
