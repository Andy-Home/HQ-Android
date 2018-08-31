package com.andy.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andy.dao.db.entity.User;

import io.reactivex.Maybe;

/**
 * Created by Andy on 2018/8/28.
 * Modify time 2018/8/28
 */
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);

    @Query("select * from users where id = :userId")
    Maybe<User> queryUser(int userId);
}
