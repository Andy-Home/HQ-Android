package com.andy.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class ToastUtils {

    public static void shortShow(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longShow(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
