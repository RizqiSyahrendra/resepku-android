package com.example.resepku;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserLoginDao {
    @Query("SELECT * FROM user_login")
    UserLogin get();

    @Insert
    void insert(UserLogin userLogin);

    @Delete
    void delete(UserLogin userLogin);

    @Query("DELETE FROM user_login")
    void clear();
}
