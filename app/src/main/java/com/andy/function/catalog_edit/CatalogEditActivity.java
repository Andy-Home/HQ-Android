package com.andy.function.catalog_edit;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.dao.db.entity.Catalog;
import com.andy.function.main.MainActivity;
import com.andy.utils.ToastUtils;

public class CatalogEditActivity extends BaseActivity implements CatalogEditContract.View{

    public static int NEW = 1;
    public static int EDIT = 2;

    public static void start(Context context, int status, int parentId, String parentName, int currentId) {
        Intent intent = new Intent(context, CatalogEditActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("parentId", parentId);
        intent.putExtra("parentName", parentName);
        intent.putExtra("currentId", currentId);
        context.startActivity(intent);
    }

    private int status;
    private int mParentId, mCurrentId;
    private String mParentName;

    private CatalogEditPresent mPresent;
    @Override
    protected void getData() {
        super.getData();

        status = dataIntent.getIntExtra("status", -1);
        if (status == -1) {
            ToastUtils.shortShow(this, "error");
            finish();
        }

        mParentId = dataIntent.getIntExtra("parentId", -1);
        if(mParentId == -1){
            ToastUtils.shortShow(this, "error");
            finish();
        }
        mCurrentId = dataIntent.getIntExtra("currentId", -1);

        mParentName = dataIntent.getStringExtra("parentName");
        mPresent = new CatalogEditPresent(this, this);
        getLifecycle().addObserver(mPresent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_catalog_edit;
    }

    private Switch vType;
    private Button vDelete, vFun;
    private ImageView vBack;
    private TextView vCategory;
    private EditText vName;

    @Override
    protected void initView() {
        vBack = findViewById(R.id.back);
        vBack.setVisibility(View.VISIBLE);

        TextView vTitle = findViewById(R.id.title);
        if (status == NEW) {
            vTitle.setText(R.string.catalog_title_new);
        } else if (status == EDIT) {
            vTitle.setText(R.string.catalog_title_edit);
        }

        vFun = findViewById(R.id.fun);
        vFun.setVisibility(View.VISIBLE);
        vFun.setText(R.string.catalog_save);

        vType = findViewById(R.id.type);

        vDelete = findViewById(R.id.delete);
        if (status == EDIT) {
            vDelete.setVisibility(View.VISIBLE);
        }

        vCategory = findViewById(R.id.category);
        if(mParentId != 0){
            vCategory.setText(mParentName);
        }

        vName = findViewById(R.id.name);

        if(status == EDIT){
            mPresent.getCatalog(mCurrentId);
        }
    }

    @Override
    protected void setListener() {
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Catalog catalog = new Catalog();
                if(status == EDIT){
                    catalog.id = mCurrentId;
                }

                catalog.name = vName.getText().toString();
                catalog.parentId = mParentId;
                catalog.userId = MainActivity.getUserId();
                if(vType.isChecked()){
                    catalog.style = 1;
                } else {
                    catalog.style = 0;
                }

                mPresent.save(catalog);
            }
        });

        vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresent.delete(mCatalog);
            }
        });
    }

    @Override
    public void saveSuccess() {
        finish();
    }

    @Override
    public void deleteSuccess() {
        finish();
    }

    private Catalog mCatalog;
    @Override
    public void displayCatalog(Catalog catalog) {
        mCatalog = catalog;
        vName.setText(catalog.name);

        if(catalog.style == 1){
            vType.setChecked(true);
        }

    }
}
