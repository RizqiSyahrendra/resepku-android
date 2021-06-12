package com.example.resepku;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserLogin.class, Meal.class, Bookmark.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserLoginDao userLoginDao();
    public abstract MealDao mealDao();
    public abstract BookmarkDao bookmarkDao();
}
