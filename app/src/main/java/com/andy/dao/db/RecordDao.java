package com.andy.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andy.dao.db.entity.Record;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Record record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Record... records);

    @Query("select * from records where `time` > :start and `time`< :end")
    Flowable<List<Record>> queryRecords(long start, long end);

    @Query("select * from records where id = :id")
    Maybe<Record> queryRecord(long id);

    @Update
    void update(Record record);

    @Update
    void update(Record... records);
}
