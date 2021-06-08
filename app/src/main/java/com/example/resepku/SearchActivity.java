package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Intent mainIntent;
    String querySearch;
    RecyclerView rvSearch;
    ArrayList<Meal> listMeal;
    ReceipesAdapter receipesAdapter;
    AppDatabase db;
    char[] alphabet;
    int ctrAlphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.search_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvSearch = findViewById(R.id.rvSearch);
        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();

        mainIntent = getIntent();
        querySearch = mainIntent.getStringExtra("query_search");
        listMeal = new ArrayList<>();
        receipesAdapter = new ReceipesAdapter(this, listMeal);
        alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        ctrAlphabet = 1;

        //jika search kosong
        if (querySearch.trim().equals("")) {
            new TaskGetListResep(this, db, "a", "", listMeal, receipesAdapter).execute();
            new TaskGetListResep(this, db, "b", "", listMeal, receipesAdapter).execute();
        }

        rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearch.setAdapter(receipesAdapter);

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (ctrAlphabet < 25) {
                        ctrAlphabet++;
                        new TaskGetListResep(SearchActivity.this, db, ""+alphabet[ctrAlphabet], "", listMeal, receipesAdapter).execute();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView)item.getActionView();
        if (!querySearch.trim().equals("")) {
            searchView.setQuery(querySearch, true);
            searchView.setIconified(false);
            submitSearch(querySearch);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Handler myHandler = new Handler();

            @Override
            public boolean onQueryTextSubmit(String query) {
                submitSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myHandler.removeCallbacksAndMessages(null);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      submitSearch(newText);
                    }
                }, 500);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void submitSearch(String q) {
        listMeal.clear();
        receipesAdapter.notifyDataSetChanged();

        if (q != null && !q.trim().equals("")) {
            new TaskGetListResep(this, db, "", q, listMeal, receipesAdapter).execute();
        }
        else {
            new TaskGetListResep(this, db, "a", "", listMeal, receipesAdapter).execute();
            new TaskGetListResep(this, db, "b", "", listMeal, receipesAdapter).execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}