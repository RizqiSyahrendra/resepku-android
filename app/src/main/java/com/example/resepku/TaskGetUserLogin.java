package com.example.resepku;

import android.os.AsyncTask;

public class TaskGetUserLogin extends AsyncTask<Void, Void, UserLogin> {
    AppDatabase db;

    public TaskGetUserLogin(AppDatabase tempDB) {
        this.db = tempDB;
    }

    @Override
    protected UserLogin doInBackground(Void... voids) {
        UserLogin userLogin = db.userLoginDao().get();
        return userLogin;
    }
}
