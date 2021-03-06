package com.andy.function.new_record;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.dao.db.entity.Record;
import com.andy.function.main.MainActivity;
import com.andy.function.new_record.entity.CatalogItem;
import com.andy.function.new_record.view.CatalogPopupWindow;
import com.andy.function.new_record.view.TextWithDropView;
import com.andy.utils.ToastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewRecordActivity extends BaseActivity implements NewRecordContract.Views {

    public static void start(Context context) {
        Intent intent = new Intent(context, NewRecordActivity.class);
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

    private EditText vMoney;
    private TextView vType;

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
        vCategory.setContentText(getResources()
                .getString(R.string.record_choose_type));
        mViewList.add(vCategory);

        Date currentDate = new Date();

        vTime = findViewById(R.id.time);
        vTime.setText(SimpleDateFormat.getDateInstance().format(currentDate));

        vCalendarView = findViewById(R.id.calendarView);
        vCalendarView.setSelectedDate(currentDate);

        vMoney = findViewById(R.id.money);
        vType = findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent = new NewRecordPresent(this);
        getLifecycle().addObserver(mPresent);
    }

    @Override
    protected void setListener() {
        vCategory.setOnClickListener(new View.OnClickListener() {
            final int index = 0;

            @Override
            public void onClick(View v) {
                if (mWindowsIndex != index || vWindow == null) {
                    mPresent.getCatalog(vCategory, 0, mCurrentType);
                } else {
                    vWindow.show();
                }
                mWindowsIndex = index;
            }
        });

        vTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vCalendarView.isShown()) {
                    vCalendarView.setVisibility(View.GONE);
                } else {
                    vCalendarView.setVisibility(View.VISIBLE);
                }
            }
        });

        vCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget,
                                       @NonNull CalendarDay date, boolean selected) {
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
                Record record = new Record();
                String money = vMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.shortShow(NewRecordActivity.this,
                            R.string.record_input_money_tips);
                    return;
                }

                if (mCurrentItem == null || mCurrentItem.id == 0) {
                    ToastUtils.shortShow(NewRecordActivity.this,
                            R.string.record_choose_catalog_tips);
                    return;
                }
                record.num = Double.valueOf(money);
                record.time = vCalendarView.getSelectedDate().getDate().getTime();
                record.typeId = mCurrentItem.id;
                record.userId = MainActivity.getUserId();
                mPresent.saveRecord(record);

                vSave.setClickable(false);
            }
        });

        vType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentType == 0) {
                    mCurrentType = 1;
                } else {
                    mCurrentType = 0;
                }
                displayType();
                mPresent.getCatalog(vCategory, 0, mCurrentType);
            }
        });
    }

    private CatalogItem mCurrentItem;
    private int mWindowsIndex = -1;
    private ArrayList<TextWithDropView> mViewList = new ArrayList<>();
    private CatalogPopupWindow vWindow = null;

    @Override
    public void showCatalogWindow(final View view, List<CatalogItem> data) {
        if (data == null || data.size() == 0) {
            vWindow = null;
            return;
        }
        vWindow = new CatalogPopupWindow(NewRecordActivity.this, view);
        vWindow.setData(data);
        vWindow.setOnItemClickListener(new CatalogPopupWindow.onItemClickListener() {
            @Override
            public void onItemClick(int position, CatalogItem item) {
                if (view instanceof TextWithDropView) {
                    mCurrentItem = item;
                    ((TextWithDropView) view).setContentText(item.text);
                    vWindow.dismiss();
                    //createTypeView(mWindowsIndex + 1);
                    displayType();
                }
            }
        });
        vWindow.show();
    }

    private int mCurrentType = 0;

    public void displayType() {
        if (mCurrentType == 0) {
            vType.setText(R.string.record_expenditure);
        } else if (mCurrentType == 1) {
            vType.setText(R.string.record_earnings);
        }
    }

    @Override
    public void saveRecordSuccess() {
        finish();
    }

    @Override
    public void onError(String msg) {
        vSave.setClickable(true);
        ToastUtils.shortShow(this, msg);
    }
}
