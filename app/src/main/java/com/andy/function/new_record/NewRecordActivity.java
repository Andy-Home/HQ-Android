package com.andy.function.new_record;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.new_record.entity.CatalogItem;
import com.andy.function.new_record.view.CatalogPopupWindow;
import com.andy.function.new_record.view.TextWithDropView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewRecordActivity extends BaseActivity implements NewRecordContract.Views {

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

    private ImageView vBack;
    private Button vSave;

    private NewRecordPresent mPresent;
    @Override
    protected void initView() {
        TextView title = findViewById(R.id.title);
        title.setText(R.string.record_title);

        vBack = findViewById(R.id.back);
        vBack.setVisibility(View.VISIBLE);
        vSave = findViewById(R.id.fun);
        vSave.setText(R.string.record_save);
        vSave.setVisibility(View.VISIBLE);

        vCategory = findViewById(R.id.category);

        Date currentDate = new Date();

        vTime = findViewById(R.id.time);
        vTime.setText(SimpleDateFormat.getDateInstance().format(currentDate));

        vCalendarView = findViewById(R.id.calendarView);
        vCalendarView.setSelectedDate(currentDate);

        mPresent = new NewRecordPresent(this);
    }


    @Override
    protected void setListener() {
        vCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vWindow == null) {
                    mPresent.getCatalog(vCategory, 0);
                } else {
                    vWindow.show();
                }
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

        vCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                vTime.setText(SimpleDateFormat.getDateInstance().format(date.getDate()));
                vCalendarView.setVisibility(View.GONE);
            }
        });

        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private CatalogItem mCurrentItem;
    private List<TextWithDropView> mViewList;
    private CatalogPopupWindow vWindow = null;

    @Override
    public void displayCatalogWindow(final View view, List<CatalogItem> data) {
        vWindow = new CatalogPopupWindow(NewRecordActivity.this, view);
        vWindow.setData(data);
        vWindow.setOnItemClickListener(new CatalogPopupWindow.onItemClickListener() {
            @Override
            public void onItemClick(int position, CatalogItem item) {
                if (view instanceof TextWithDropView) {
                    mCurrentItem = item;
                    ((TextWithDropView) view).setContentText(item.text);
                    vWindow.dismiss();
                }
            }
        });
        vWindow.show();
    }
}
