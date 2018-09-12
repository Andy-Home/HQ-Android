package com.andy.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andy.dao.db.entity.Record;
import com.andy.dao.db.entity.RecordContent;
import com.andy.dao.db.entity.RecordStatistics;

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

    @Query("select count(records.num) as num, catalogs.name, records.type_id, catalogs.style " +
            "from catalogs " +
            "left join records " +
            "on records.type_id = catalogs.id " +
            "where catalogs.parent_id = :parentId " +
            "and records.time < :start and records.time > :end ")
    Flowable<List<RecordStatistics>> queryRecordStatistics(long start, long end, int parentId);

    @Query("select total(records.num) " +
            "from records " +
            "where records.type_id in (:id) " +
            "and records.time < :start and records.time > :end ")
    Maybe<Double> queryRecordTotal(long start, long end, Integer... id);

    @Query("select * from records where id = :id")
    Maybe<Record> queryRecord(long id);

    @Update
    void update(Record record);

    @Update
    void update(Record... records);
}
