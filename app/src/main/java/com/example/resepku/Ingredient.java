package com.example.resepku;

public class Ingredient {
    private String name, measure;

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getImage() {
        return Endpoint.MEAL_API_IMG_INGREDIENT + this.name;
    }
}
