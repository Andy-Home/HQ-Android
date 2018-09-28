package com.andy.dao;

/**
 * Created by Andy on 2018/9/27.
 * Modify time 2018/9/27
 */
public interface BaseListener<T> {

    void onSuccess(T t);

    void onError(String msg);
}
