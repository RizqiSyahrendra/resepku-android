package com.example.resepku;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TaskGetListResep extends AsyncTask<Void, Void, List<Meal>> {
    private Context context;
    private AppDatabase db;
    private String first, search;
    private ArrayList<Meal> meals;
    private ReceipesAdapter receipesAdapter;
    private UserLogin userLogin;

    public TaskGetListResep(Context context, AppDatabase db, String first, String search, ArrayList<Meal> meals, ReceipesAdapter receipesAdapter) {
        this.context = context;
        this.db = db;
        this.first = first;
        this.search = search;
        this.meals = meals;
        this.receipesAdapter = receipesAdapter;
    }

    @Override
    protected List<Meal> doInBackground(Void... voids) {
        userLogin = db.userLoginDao().get();
        List<Meal> tempMeals = db.mealDao().getAll();
        return tempMeals;
    }

    @Override
    protected void onPostExecute(List<Meal> tempMeals) {
        if (tempMeals.size() > 0) {
            meals.addAll(tempMeals);
            receipesAdapter.notifyDataSetChanged();
        }
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.api_meal_list), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);

                        //fill list meal
                        JSONArray datas = responseObj.getJSONArray("data");
                        for (int i=0; i < datas.length(); i++) {
                            JSONObject m = datas.getJSONObject(i);
                            Meal meal = new Meal(
                                    m.getInt("id"),
                                    m.getString("name"),
                                    "",
                                    "",
                                    "",
                                    m.getString("image"),
                                    0
                            );

                            new TaskAddResep(db).execute(meal);
                            meals.add(meal);
                        }

                        receipesAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String res = new String(error.networkResponse.data);
                    try {
                        JSONObject resObj = new JSONObject(res);
                        Toast.makeText(context, resObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                        body.put("first", "a");
                        body.put("search", "");
                        return body.toString().getBytes(StandardCharsets.UTF_8);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }

}
