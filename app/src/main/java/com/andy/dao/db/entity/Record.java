package com.andy.dao.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "records")
public class Record {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "type_id")
    public int typeId;

    public double num;

    @ColumnInfo(name = "user_id")
    public int userId;

    public long time;
}
