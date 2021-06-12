package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    public AppDatabase db;
    public UserLogin userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();

        try {
            userLogin = new TaskGetUserLogin(db).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.btnMenuHome);
        }
    }

    public AppDatabase getDB() {
        return db;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }


    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.btnMenuHome) {
            fragment = FragmentHome.newInstance(this);
        }
        if (item.getItemId() == R.id.btnMenuBookmark) {
            fragment = FragmentBookmark.newInstance(this);
        }
        if (item.getItemId() == R.id.btnMenuSettings) {
            fragment = FragmentSetting.newInstance(this);
        }

        return loadFragment(fragment);
    }
}