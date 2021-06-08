package com.example.resepku;

import android.os.AsyncTask;

public class TaskAddResep extends AsyncTask<Meal, Void, Void> {
    AppDatabase db;

    public TaskAddResep(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(Meal... meals) {
        db.mealDao().insert(meals[0]);
        return null;
    }
}
