package com.andy.dao;

/**
 * Created by Andy on 2018/9/27.
 * Modify time 2018/9/27
 */
public interface BaseListener {

    void onSuccess(Object... o);

    void onError(String msg);
}
