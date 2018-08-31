package com.andy.dao.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
@Entity(tableName = "catalogs")
public class Catalog {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public int style;   //0为支出，1为收入

    @ColumnInfo(name = "parent_id")
    public int parentId;

    @ColumnInfo(name = "user_id")
    public int userId;  //创建者
}
