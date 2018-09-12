package com.andy.utils;

import android.content.Context;

/**
 * Created by Andy on 2018/9/12.
 * Modify time 2018/9/12
 */
public class DPValueUtil {

    public static DPValueUtil getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        private final static DPValueUtil mInstance = new DPValueUtil();
    }

    private static float scale = 1f;

    public void init(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }
}
