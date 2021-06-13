package com.example.resepku;

public class Comment {
    public int id, user_id;
    public String name, text, date, img;

    public Comment(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public Comment(int id, int user_id, String name, String text, String date, String img) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.text = text;
        this.date = date;
        this.img  = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
