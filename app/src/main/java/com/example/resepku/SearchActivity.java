package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Intent mainIntent;
    String querySearch;
    RecyclerView rvSearch;
    ArrayList<Meal> listMeal;
    ReceipesAdapter receipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.search_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvSearch = findViewById(R.id.rvSearch);

        mainIntent = getIntent();
        querySearch = mainIntent.getStringExtra("query_search");
        listMeal = new ArrayList<>();
        receipesAdapter = new ReceipesAdapter(this, listMeal);
        if (querySearch.trim().equals("")) {
            submitSearch(querySearch);
        }

        rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearch.setAdapter(receipesAdapter);
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
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void submitSearch(String q) {
        listMeal.add(new Meal("1", "Bakso", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("2", "Mie", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("3", "Nasi Goreng", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("4", "Gado - Gado", "makanan", "Indonesia", "tes"));
        receipesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}