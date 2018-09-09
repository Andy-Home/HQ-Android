package com.andy.dao.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.Record;
import com.andy.dao.db.entity.User;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
@Database(entities = {User.class, Catalog.class, Record.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract CatalogDao catalogDao();

    public abstract RecordDao recordDao();
}
