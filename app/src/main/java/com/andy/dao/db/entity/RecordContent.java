package com.andy.dao.db.entity;

import android.arch.persistence.room.ColumnInfo;

public class RecordContent {
    public int id;

    @ColumnInfo(name = "name")
    public String catalogName;

    @ColumnInfo(name = "type_id")
    public int catalog;

    @ColumnInfo(name = "style")
    public int type;       //0为支出，1为收入

    public long time;

    public double num;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "head_url")
    public String headUrl;

    public int status = 0;      //0为默认显示，1为时间的开始数据
}
