package com.example.resepku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    public final int splashInterval = 1500;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        db = Room.databaseBuilder(SplashActivity.this, AppDatabase.class, "db_resep").build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetUserLoginTask().execute();
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, splashInterval);
    }

    private class GetUserLoginTask extends AsyncTask<Void, Void, UserLogin> {
        @Override
        protected UserLogin doInBackground(Void... voids) {
            UserLogin userLogin = db.userLoginDao().get();
            return userLogin;
        }

        @Override
        protected void onPostExecute(UserLogin userLogin) {
            if (userLogin != null) {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            super.onPostExecute(userLogin);
        }
    }
}