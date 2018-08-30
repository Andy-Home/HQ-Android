package com.andy.function.catalog_edit;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.utils.ToastUtils;

public class CatalogEditActivity extends BaseActivity {

    public static int NEW = 1;
    public static int EDIT = 2;

    public static void start(Context context, int status) {
        Intent intent = new Intent(context, CatalogEditActivity.class);
        intent.putExtra("status", status);
        context.startActivity(intent);
    }

    private int status;

    @Override
    protected void getData() {
        super.getData();

        status = dataIntent.getIntExtra("status", 0);
        if (status == 0) {
            ToastUtils.shortShow(this, "error");
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_catalog_edit;
    }

    private Switch vType;
    private Button vDelete, vFun;
    private ImageView vBack;

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
                finish();
            }
        });

        vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
