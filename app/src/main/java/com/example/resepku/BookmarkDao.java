package com.example.resepku;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmark where user_id = :user_id")
    List<Bookmark> getAll(int user_id);

    @Query("SELECT COUNT(*) from bookmark where user_id=:user_id and food_id=:food_id")
    int countUserAndFood(int user_id, int food_id);

    @Insert
    void insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("DELETE FROM bookmark")
    void clear();

}
