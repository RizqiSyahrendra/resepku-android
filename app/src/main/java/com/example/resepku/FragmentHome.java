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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FragmentHome extends Fragment {

    RecyclerView rvHomeReceipes;
    EditText txtHomeSearch;
    MaterialButton btnHomeSearch, btnHomeViewAll;
    ArrayList<Meal> listMeal;
    MainActivity parent;

    public static FragmentHome newInstance(MainActivity tempParent) {
        FragmentHome fragment = new FragmentHome();
        fragment.parent = tempParent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMeal = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        //set adapter to recyclerview
        ReceipesAdapter receipesAdapter = new ReceipesAdapter(getContext(), listMeal);
        rvHomeReceipes.setAdapter(receipesAdapter);

        //fill data to adapter
        new TaskGetListResep(getContext(), parent.getDB(), "a", "", listMeal, receipesAdapter).execute();

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