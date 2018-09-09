package com.andy.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andy.dao.db.entity.Record;
import com.andy.dao.db.entity.RecordContent;

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

    @Query("select * from records where `time` > :start and `time`< :end limit 0, :num")
    Flowable<List<Record>> queryRecords(long start, long end, int num);

    @Query("select records.*, users.head_url, catalogs.name, catalogs.style, 0 as status " +
            "from records " +
            "left join catalogs " +
            "on records.type_id = catalogs.id " +
            "left join users " +
            "on users.id = records.user_id " +
            "where records.time < :start " +
            "and records.time > :end " +
            "order by time desc " +
            "limit 0, :num")
    Flowable<List<RecordContent>> queryRecordContents(long start, long end, int num);

    @Query("select * from records where id = :id")
    Maybe<Record> queryRecord(long id);

    @Update
    void update(Record record);

    @Update
    void update(Record... records);
}