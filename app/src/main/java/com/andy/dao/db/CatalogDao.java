package com.andy.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andy.dao.db.entity.Catalog;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
@Dao
public interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Catalog catalog);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Catalog ... catalogs);

    @Query("select * from catalogs")
    Flowable<List<Catalog>> queryAll();

    @Query("select * from catalogs where parent_id = :parentId")
    Flowable<List<Catalog>> queryCatalogList(int parentId);

    @Query("with recursive " +
            "c as ( " +
            "select * from catalogs where parent_id = :id " +
            "union all " +
            "select catalogs.* " +
            "from c " +
            "join catalogs on c.id = catalogs.parent_id) " +
            "select * from c")
    Flowable<List<Catalog>> queryCatalogAll(int id);

    @Query("select * from catalogs where id = :id")
    Maybe<Catalog> queryCatalog(int id);

    @Delete
    void deleteCatalog(Catalog catalog);
}
