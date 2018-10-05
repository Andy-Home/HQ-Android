package com.andy.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

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

    @SuppressLint("ApplySharedPref")
    public void putUserId(int userId) {
        mUserId = userId;
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", userId);
        editor.commit();
    }

    public void putLoginChannel(String channel) {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("channel", channel);
        editor.apply();
    }

    public String getLoginChannel() {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("channel", "");
    }

    public void putQQLoginStates(String token, String expires, String openId) {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("qq_login_token", token);
        editor.putString("qq_login_expires", expires);
        editor.putString("qq_login_openId", openId);

        editor.apply();
    }

    public Map<String, String> getQQLoginStates() {
        Map<String, String> map = new HashMap<>();
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = preferences.getString("qq_login_token", "");
        String expires = preferences.getString("qq_login_expires", "");
        String openId = preferences.getString("qq_login_openId", "");

        map.put("token", token);
        map.put("expires", expires);
        map.put("openId", openId);
        return map;
    }

    private static long updateTime = 0;

    public void syncUpdateTime(long updateTime) {
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("updateTime", updateTime);
        editor.apply();
    }

    public long getUpdateTime() {
        if (updateTime != 0) {
            return updateTime;
        }
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getLong("updateTime", 0);
    }

    private static long endTime = 0;

    public void syncEndTime(long endTime) {
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("endTime", endTime);
        editor.apply();
    }

    public long getEndTime() {
        if (endTime != 0) {
            return endTime;
        }
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getLong("endTime", 0);
    }

    private static long startTime = 0;

    public void syncStartTime(long startTime) {
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("startTime", startTime);
        editor.apply();
    }

    public long getStartTime() {
        if (startTime != 0) {
            return startTime;
        }
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getLong("startTime", 0);
    }

    @SuppressLint("ApplySharedPref")
    public void putHomeUsers(String users) {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        homeUsers = users.split(",");
        editor.putString("homeUsers", users);
        editor.commit();
    }

    private static String[] homeUsers = new String[4];

    public String[] getHomeUsers() {
        if (homeUsers.length != 0) {
            return homeUsers;
        }
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        String users = preferences.getString("homeUsers", "" + mUserId);
        return users.split(",");
    }
}
