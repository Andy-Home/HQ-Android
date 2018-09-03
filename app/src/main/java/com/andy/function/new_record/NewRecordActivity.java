package com.andy.function.new_record;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.utils.ToastUtils;
import com.andy.view.TextWithDropView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class NewRecordActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,NewRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_record;
    }

    private TextWithDropView vCategory;
    private Button vTime;
    private MaterialCalendarView vCalendarView;
    @Override
    protected void initView() {
        vCategory = findViewById(R.id.category);

        vTime = findViewById(R.id.time);
        vCalendarView = findViewById(R.id.calendarView);
    }

    @Override
    protected void setListener() {
        vCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.shortShow(NewRecordActivity.this, "点击了按钮");
            }
        });

        vTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vCalendarView.isShown()){
                    vCalendarView.setVisibility(View.GONE);
                }else {
                    vCalendarView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
