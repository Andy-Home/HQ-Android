package com.andy.function.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.andy.BaseActivity;
import com.andy.R;
import com.andy.function.main.chart.ChartFragment;
import com.andy.function.main.order.OrderFragment;
import com.andy.function.main.person.PersonFragment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class MainActivity extends BaseActivity {

    private static int userId = 0;
    public static int getUserId(){
        return userId;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    private BottomNavigationView vBottomNavigationView;
    private ViewPager vViewPager;

    @Override
    protected void initView() {
        OrderFragment mOrderFragment = new OrderFragment();
        ChartFragment mChartFragment = new ChartFragment();
        PersonFragment mPersonFragment = new PersonFragment();

        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mOrderFragment);
        mFragmentList.add(mChartFragment);
        mFragmentList.add(mPersonFragment);

        vViewPager = findViewById(R.id.content);
        vViewPager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));

        vBottomNavigationView = findViewById(R.id.bottom_tab);
    }

    @Override
    protected void setListener() {
        vBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.item1:
                        vViewPager.setCurrentItem(0);
                        return true;
                    case R.id.item2:
                        vViewPager.setCurrentItem(1);
                        return true;

                    case R.id.item3:
                        vViewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        vViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        vBottomNavigationView.setSelectedItemId(R.id.item1);
                        break;
                    case 1:
                        vBottomNavigationView.setSelectedItemId(R.id.item2);
                        break;
                    case 2:
                        vBottomNavigationView.setSelectedItemId(R.id.item3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() { }
}
