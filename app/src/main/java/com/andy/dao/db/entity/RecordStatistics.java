package com.andy.dao.db.entity;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class RecordStatistics {

    @ColumnInfo(name = "type_id")
    public int catalog;

    @ColumnInfo(name = "name")
    public String catalogName;

    public double num;

    @ColumnInfo(name = "style")
    public int type;       //0为支出，1为收入
}
