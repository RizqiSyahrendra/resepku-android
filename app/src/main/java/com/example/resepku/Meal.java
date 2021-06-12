package com.example.resepku;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal")
public class Meal {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "area")
    private String area;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "ingredients")
    private String ingredients;

    public Meal(int id, String name, String category, String area, String instructions, String image, float rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.image = image;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rate) {
        this.rating = rate;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(String ing) {
        this.ingredients = ing;
    }


}
