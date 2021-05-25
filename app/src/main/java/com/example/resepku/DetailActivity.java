package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    RecyclerView rvDetailIngredients;
    TextView txtDetailInstructions;
    ArrayList<Ingredient> listIngredient;
    FrameLayout commentFragmentContainer;
    ImageView imgDetailBookmark;
    RatingBar ratingDetail;
    private boolean isBookmarked;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(R.string.detail_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvDetailIngredients = findViewById(R.id.rvDetailIngredients);
        txtDetailInstructions = findViewById(R.id.txtDetailInstructions);
        commentFragmentContainer = findViewById(R.id.commentFragmentContainer);
        imgDetailBookmark = findViewById(R.id.imgDetailBookmark);
        ratingDetail = findViewById(R.id.ratingDetail);
        listIngredient = new ArrayList<>();
        isBookmarked = false;

        loadDetailData();
        loadFragment(new FragmentComments());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.btnMenuRate) {
            openRateWindow();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRateWindow() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View rateView = getLayoutInflater().inflate(R.layout.window_rate, null);

        RatingBar btnRateInput = rateView.findViewById(R.id.btnRateInput);
        Button btnRateCancel = rateView.findViewById(R.id.btnRateCancel);
        Button btnRateOk = rateView.findViewById(R.id.btnRateOk);

        btnRateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               submitRating(btnRateInput.getRating());
               dialog.dismiss();
            }
        });

        dialogBuilder.setView(rateView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void submitRating(float rate) {
        Toast.makeText(this, "Rate : " + rate, Toast.LENGTH_SHORT).show();
    }

    private void loadDetailData() {
        //isi ingredients
        listIngredient.add(new Ingredient("Onion", "2 slices"));
        listIngredient.add(new Ingredient("Onion", "2 slices"));
        listIngredient.add(new Ingredient("Onion", "2 slices"));
        listIngredient.add(new Ingredient("Onion", "2 slices"));
        ratingDetail.setRating((float) 2.5);
        rvDetailIngredients.setLayoutManager(new GridLayoutManager(this, 3));
        rvDetailIngredients.setAdapter(new IngredientsAdapter(this, listIngredient));

        //isi instructions
        String instructions = "Mix all the ingredients in a bowl.\r\nHeat a pan or griddle with a little vegetable oil.\r\nPour the mixture onto the pan and place a piece of open-faced baguette on top.\r\nMix all the ingredients in a bowl.\n" +
                "Heat a pan or griddle with a little vegetable oil.\n" +
                "Pour the mixture onto the pan and place a piece of open-faced baguette on top.\n";
        txtDetailInstructions.setText(instructions);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.commentFragmentContainer, fragment).commit();
            return true;
        }

        return false;
    }

    public void onClickBookmark(View view) {
        if (isBookmarked) {
            imgDetailBookmark.setImageResource(R.drawable.ic_bookmark);
            isBookmarked = false;
        }
        else {
            imgDetailBookmark.setImageResource(R.drawable.ic_bookmarked);
            isBookmarked = true;
        }
    }
}