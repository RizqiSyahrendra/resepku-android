package com.example.resepku;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class FragmentHome extends Fragment {

    RecyclerView rvHomeReceipes;
    EditText txtHomeSearch;
    MaterialButton btnHomeSearch, btnHomeViewAll;
    ArrayList<Meal> listMeal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMeal = new ArrayList<>();
        listMeal.add(new Meal("1", "Bakso", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("2", "Mie", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("3", "Nasi Goreng", "makanan", "Indonesia", "tes"));
        listMeal.add(new Meal("4", "Gado - Gado", "makanan", "Indonesia", "tes"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtHomeSearch = view.findViewById(R.id.txtHomeSearch);
        btnHomeSearch = view.findViewById(R.id.btnHomeSearch);
        btnHomeViewAll = view.findViewById(R.id.btnHomeViewAll);
        rvHomeReceipes = view.findViewById(R.id.rvHomeReceipes);
        rvHomeReceipes.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ReceipesAdapter receipesAdapter = new ReceipesAdapter(getContext(), listMeal);
        rvHomeReceipes.setAdapter(receipesAdapter);

        btnHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String querySearch = txtHomeSearch.getText().toString().trim();
                goToBrowseRecipe(querySearch);
            }
        });

        btnHomeViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBrowseRecipe("");
            }
        });
    }

    private void goToBrowseRecipe(String querySearch) {
        Intent searchIntent = new Intent(getContext(), SearchActivity.class);
        searchIntent.putExtra("query_search", querySearch);
        startActivity(searchIntent);
    }
}