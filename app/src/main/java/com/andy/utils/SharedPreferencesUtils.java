package com.andy.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andy on 2018/9/26.
 * Modify time 2018/9/26
 */
public class SharedPreferencesUtils {

    private Context mContext;

    private SharedPreferencesUtils() {
    }

    static class SingleHolder {
        @SuppressLint("StaticFieldLeak")
        final static SharedPreferencesUtils mInstance = new SharedPreferencesUtils();
    }

    public static synchronized SharedPreferencesUtils getInstance() {
        return SingleHolder.mInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    private static long mCatalogModifyTime = 0;

    public void putCatalogModifyTime(long time) {
        mCatalogModifyTime = time;
        SharedPreferences preferences = mContext.getSharedPreferences("modifyTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("mCatalogModifyTime", time);
        editor.apply();
    }

    public long getCatalogModifyTime() {
        if (mCatalogModifyTime != 0) {
            return mCatalogModifyTime;
        }

        SharedPreferences preferences = mContext.getSharedPreferences("modifyTime", Context.MODE_PRIVATE);
        return preferences.getLong("mCatalogModifyTime", 0);
    }

    private static int mUserId = 0;

    public int getUserId() {
        if (mUserId != 0) {
            return mUserId;
        }
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getInt("userId", 0);
    }

    public void putUserId(int userId) {
        mUserId = userId;
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", userId);
        editor.apply();
    }
}