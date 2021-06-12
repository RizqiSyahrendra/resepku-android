package com.example.resepku;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bookmark", primaryKeys = {"user_id", "food_id"})
public class Bookmark {
    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "food_id")
    private int food_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "image")
    private String image;

    public Bookmark(int user_id, int food_id, String name, String image) {
        this.user_id = user_id;
        this.food_id = food_id;
        this.name = name;
        this.image = image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Meal toMeal() {
        Meal meal = new Meal(this.food_id, this.name, "", "", "", this.image, 0);
        return meal;
    }
}
