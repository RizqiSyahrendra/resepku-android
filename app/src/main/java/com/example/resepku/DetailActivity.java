package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    RecyclerView rvDetailIngredients;
    TextView txtDetailInstructions, txtDetailFoodName, txtDetailFoodCategory, txtDetailFoodArea;
    ArrayList<Ingredient> listIngredient;
    FrameLayout commentFragmentContainer;
    ImageView imgDetailBookmark, imgDetailResep;
    RatingBar ratingDetail;
    private boolean isBookmarked;
    AlertDialog dialog;
    String idMeal;
    AppDatabase db;
    UserLogin userLogin;
    IngredientsAdapter ingredientsAdapter;
    ProgressBar pgBarIngredients, pgBarDetailHeader;
    LinearLayout layoutDetailHeaderRight;
    Meal activeMeal;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(R.string.detail_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();
        rvDetailIngredients = findViewById(R.id.rvDetailIngredients);
        txtDetailInstructions = findViewById(R.id.txtDetailInstructions);
        commentFragmentContainer = findViewById(R.id.commentFragmentContainer);
        imgDetailBookmark = findViewById(R.id.imgDetailBookmark);
        imgDetailResep = findViewById(R.id.imgDetailResep);
        ratingDetail = findViewById(R.id.ratingDetail);
        pgBarIngredients = findViewById(R.id.pgBarIngredients);
        pgBarDetailHeader = findViewById(R.id.pgBarDetailHeader);
        layoutDetailHeaderRight = findViewById(R.id.layoutDetailHeaderRight);
        listIngredient = new ArrayList<>();
        isBookmarked = false;
        txtDetailFoodName = findViewById(R.id.txtDetailFoodName);
        txtDetailFoodCategory = findViewById(R.id.txtDetailFoodCategory);
        txtDetailFoodArea = findViewById(R.id.txtDetailFoodArea);
        idMeal = getIntent().getStringExtra("id_meal");
        mainIntent = getIntent();

        try {
            userLogin = new TaskGetUserLogin(db).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pgBarIngredients.setVisibility(View.VISIBLE);
        pgBarDetailHeader.setVisibility(View.VISIBLE);
        layoutDetailHeaderRight.setVisibility(View.GONE);
        ingredientsAdapter = new IngredientsAdapter(this, listIngredient);
        rvDetailIngredients.setLayoutManager(new GridLayoutManager(this, 3));
        rvDetailIngredients.setAdapter(ingredientsAdapter);

        loadDetailData();
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public Meal getActiveMeal() {
        return activeMeal;
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
        TextView titleWindowRate = rateView.findViewById(R.id.titleWindowRate);

        btnRateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (activeMeal.getYourRating() <= 0) {
            btnRateOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitRating(btnRateInput.getRating());
                    dialog.dismiss();
                }
            });
            btnRateCancel.setText("Cancel");
        }
        else {
            titleWindowRate.setText(getString(R.string.rate_title_after));
            btnRateInput.setRating(activeMeal.getYourRating());
            btnRateInput.setEnabled(false);
            btnRateOk.setVisibility(View.GONE);
            btnRateCancel.setText("Close");
        }

        dialogBuilder.setView(rateView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void submitRating(float rate) {
        if(rate < 0.5) {
            Toast.makeText(this, "Rating is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.api_set_rating), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    String resultMessage = responseObj.getString("message");

                    Toast.makeText(DetailActivity.this, resultMessage, Toast.LENGTH_SHORT).show();
                    new TaskGetDetailResep(true, true).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String body = new String(error.networkResponse.data);
                    try {
                        JSONObject result = new JSONObject(body);
                        Toast.makeText(DetailActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject body = new JSONObject();
                    body.put("user_id", userLogin.getId());
                    body.put("food_id", idMeal);
                    body.put("rating", rate);
                    body.put("token", userLogin.getAccessToken());
                    return body.toString().getBytes(StandardCharsets.UTF_8);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Toast.makeText(this, "Thank you for your rating!", Toast.LENGTH_SHORT).show();
    }

    private void loadDetailData() {
        new TaskGetDetailResep().execute();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.commentFragmentContainer, fragment).commit();
            return true;
        }

        return false;
    }

    private void setBookmark() {
        if (isBookmarked) {
            imgDetailBookmark.setImageResource(R.drawable.ic_bookmarked);
        }
        else {
            imgDetailBookmark.setImageResource(R.drawable.ic_bookmark);
        }
    }

    public void onClickBookmark(View view) {
        Bookmark bookmark = new Bookmark(userLogin.getId(), activeMeal.getId(), activeMeal.getName(), activeMeal.getImage());

        if (isBookmarked) {
            new TaskRemoveBookmark().execute(bookmark);
        }
        else {
            new TaskSaveBookmark(db).execute(bookmark);
        }

        StringRequest setBookMarkRequest = new StringRequest(Request.Method.POST, getString(R.string.api_set_bookmark), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    Toast.makeText(DetailActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null){
                    String res = new String(error.networkResponse.data);
                    try {
                        JSONObject resObj = new JSONObject(res);
                        Toast.makeText(DetailActivity.this, resObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject body = new JSONObject();
                    body.put("token", userLogin.getAccessToken());
                    body.put("user_id", userLogin.getId());
                    body.put("food_id", activeMeal.getId());
                    return body.toString().getBytes(StandardCharsets.UTF_8);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(setBookMarkRequest);

        isBookmarked = !isBookmarked;
        setBookmark();
    }

    private void setMealToView(Meal meal, boolean updateRatingOnly) {
        activeMeal = meal;
        ratingDetail.setRating(meal.getRating());
        if (updateRatingOnly) {
            return;
        }

        Picasso.get().load(meal.getImage()).placeholder(R.drawable.food_placeholder).into(imgDetailResep);
        txtDetailFoodName.setText(meal.getName());
        txtDetailFoodCategory.setText(meal.getCategory());
        txtDetailFoodArea.setText(meal.getArea());
        txtDetailInstructions.setText(meal.getInstructions());
        pgBarIngredients.setVisibility(View.GONE);
        pgBarDetailHeader.setVisibility(View.GONE);
        layoutDetailHeaderRight.setVisibility(View.VISIBLE);

        listIngredient.clear();
        String ingredients = meal.getIngredients();
        if (ingredients != null) {
            try {
                JSONArray ingredientJSON = new JSONArray(ingredients);
                for (int i=0; i < ingredientJSON.length(); i++) {
                    JSONObject ingredientObj = ingredientJSON.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(
                            ingredientObj.getString("ingredient"),
                            ingredientObj.getString("measure"),
                            ingredientObj.getString("image")
                    );

                    listIngredient.add(ingredient);
                }

                ingredientsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        loadFragment(FragmentComments.newInstance(this));
    }

    private class TaskGetDetailResep extends AsyncTask<Void, Void, Meal> {
        public boolean updateNew;
        public boolean updateRatingOnly;

        public TaskGetDetailResep(boolean paramUpdateNew, boolean updateRatingOnly) {
            this.updateNew = paramUpdateNew;
            this.updateRatingOnly = updateRatingOnly;
        }

        public TaskGetDetailResep() {
            this.updateNew = false;
            this.updateRatingOnly = false;
        }


        @Override
        protected Meal doInBackground(Void... voids) {
            if (updateNew) {
                return activeMeal;
            }

            int id = Integer.parseInt(idMeal);
            return db.mealDao().getOne(id);
        }

        @Override
        protected void onPostExecute(Meal meal) {
            super.onPostExecute(meal);
            if (meal != null && !meal.getCategory().equals("") && !updateNew) {
                setMealToView(meal, updateRatingOnly);
                new TaskCheckBookmark().execute();
            }
            else {
                StringRequest detailMealRequest = new StringRequest(Request.Method.POST, getString(R.string.api_meal_detail), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONObject data = responseObj.getJSONObject("data");

                            meal.setCategory(data.getString("tag"));
                            meal.setArea(data.getString("place"));
                            meal.setImage(data.getString("image"));
                            meal.setInstructions(data.getString("instructions"));
                            meal.setIngredients(data.getJSONArray("ingredient").toString());
                            meal.setRating((float) data.getDouble("rating"));
                            meal.setYourRating((float) data.getDouble("your_rating"));

                            new TaskUpdateDetailResep().execute(meal);
                            setMealToView(meal, updateRatingOnly);

                            //check bookmark
                            if (data.getBoolean("is_bookmarked")) {
                                isBookmarked = true;
                                setBookmark();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null){
                            String res = new String(error.networkResponse.data);
                            try {
                                JSONObject resObj = new JSONObject(res);
                                Toast.makeText(DetailActivity.this, resObj.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            JSONObject body = new JSONObject();
                            body.put("token", userLogin.getAccessToken());
                            body.put("id", Integer.parseInt(idMeal));
                            return body.toString().getBytes(StandardCharsets.UTF_8);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
                requestQueue.add(detailMealRequest);
            }
        }
    }

    private class TaskUpdateDetailResep extends AsyncTask<Meal, Void, Void> {
        @Override
        protected Void doInBackground(Meal... meals) {
            db.mealDao().update(meals[0]);
            return null;
        }
    }

    private class TaskRemoveBookmark extends AsyncTask<Bookmark, Void, Void> {
        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            db.bookmarkDao().delete(bookmarks[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainIntent.putExtra("action", "refresh_bookmarks");
            setResult(100, mainIntent);
        }
    }

    private class TaskCheckBookmark extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return db.bookmarkDao().countUserAndFood(userLogin.getId(), activeMeal.getId());
        }

        @Override
        protected void onPostExecute(Integer jumlah) {
            super.onPostExecute(jumlah);
            if (jumlah > 0) {
                isBookmarked = true;
                setBookmark();
            }
        }
    }


}