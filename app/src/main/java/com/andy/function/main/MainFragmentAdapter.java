package com.andy.function.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Andy on 2018/8/29.
 * Modify time 2018/8/29
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList;

    public MainFragmentAdapter(FragmentManager fm,@NotNull ArrayList<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
