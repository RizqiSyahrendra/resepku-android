package com.example.resepku;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentBookmark extends Fragment {

    RecyclerView rvBookmarksReceipes;
    ArrayList<Meal> listMeal;

    public FragmentBookmark() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBookmarksReceipes = view.findViewById(R.id.rvBookmarksReceipes);
        rvBookmarksReceipes.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ReceipesAdapter receipesAdapter = new ReceipesAdapter(getContext(), listMeal);
        rvBookmarksReceipes.setAdapter(receipesAdapter);
    }
}