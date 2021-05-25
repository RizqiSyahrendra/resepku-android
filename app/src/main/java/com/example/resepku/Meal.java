package com.example.resepku;

public class Meal {
    private String id, name, category, area, instructions;
    private float rating;

    public Meal(String id, String name, String category, String area, String instructions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return this.getRating();
    }

    public void setRating(float rate) {
        this.rating = rate;
    }

    public String getImage() {
        return Endpoint.MEAL_API_IMG_MEAL + this.name;
    }
}
