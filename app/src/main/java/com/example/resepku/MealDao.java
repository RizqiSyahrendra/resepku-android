package com.example.resepku;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    List<Meal> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Query("DELETE FROM meal")
    void clear();
}
