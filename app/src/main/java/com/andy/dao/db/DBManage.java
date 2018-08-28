package com.andy.dao.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.text.TextUtils;

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
            db = Room.databaseBuilder(context,AppDataBase.class,"hq.db").build();
        }
    }

    public UserDao getUserDao() throws Exception {
        checkNull();
        return db.userDao();
    }

    private void checkNull() throws Exception {
        if(db ==null){
            throw new Exception("DBManage is not init");
        }
    }
}
