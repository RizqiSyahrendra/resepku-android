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

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.btnMenuHome);
        }
    }

    public AppDatabase getDB() {
        return db;
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
            fragment = new FragmentHome();
        }
        if (item.getItemId() == R.id.btnMenuBookmark) {
            fragment = new FragmentBookmark();
        }
        if (item.getItemId() == R.id.btnMenuSettings) {
            fragment = FragmentSetting.newInstance(this);
        }

        return loadFragment(fragment);
    }
}