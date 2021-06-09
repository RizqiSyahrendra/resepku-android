package com.example.resepku;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    List<Meal> getAll();

    @Query("SELECT * FROM meal WHERE id=:id")
    Meal getOne(int id);

    @Query("SELECT * FROM meal WHERE lower(substr(name, 1, 1)) = :first")
    List<Meal> getByFirstLetter(String first);

    @Query("SELECT * FROM meal WHERE name LIKE '%' || :q || '%'")
    List<Meal> getByName(String q);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Meal meal);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);

    @Query("DELETE FROM meal")
    void clear();
}
