package com.andy.dao.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nick_name")
    public String nickName;

    @ColumnInfo(name = "head_url")
    public String headUrl;

    /**
     * 类型，0为正常用户账号，1为家庭账号
     */
    public int type;

    /**
     * 家庭账号
     */
    @ColumnInfo(name = "home_id")
    public int homeId;


}
