package com.andy.dao.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
public class DBManage {

    private DBManage(){ }

    public static DBManage getInstance() {
        return SingletonHolder.mInstance;
    }

    private static class SingletonHolder{
        private static final DBManage mInstance = new DBManage();
    }

    private AppDataBase db = null;
    public void init(@NotNull Context context){
        if(db == null){
            db = Room.databaseBuilder(context, AppDataBase.class, "hq.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "`type_id` INTEGER NOT NULL,"
                    + "`user_id` INTEGER NOT NULL,"
                    + "`num` REAL NOT NULL,"
                    + "`time` INTEGER NOT NULL)");
        }
    };

    public UserDao getUserDao(){
        try {
            checkNull();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return db.userDao();
    }

    public CatalogDao getCatalogDao(){
        try {
            checkNull();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return db.catalogDao();
    }

    public RecordDao getRecordDao() {
        try {
            checkNull();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return db.recordDao();
    }

    private void checkNull() throws Exception {
        if(db ==null){
            throw new Exception("DBManage is not init");
        }
    }
}
