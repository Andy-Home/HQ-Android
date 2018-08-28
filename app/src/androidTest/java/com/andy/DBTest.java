package com.andy;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.andy.dao.db.AppDataBase;
import com.andy.dao.db.UserDao;
import com.andy.dao.db.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
@RunWith(AndroidJUnit4.class)
public class DBTest {

    private UserDao mUserDao;
    private AppDataBase mAppDataBase;

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getTargetContext();
        mAppDataBase = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
        mUserDao = mAppDataBase.userDao();
    }

    @After
    public void closeDb(){
        mAppDataBase.close();
    }

    @Test
    public void userDaoInsert(){
        User user = new User();
        user.id=1;
        user.nickName="test";
        user.type=0;
        user.homeId=4;
        user.headUrl="123";

        mUserDao.insertUser(user);

        User user1 = mUserDao.queryUser(1);
        System.out.println("nickName:"+user1.nickName);
    }
}
