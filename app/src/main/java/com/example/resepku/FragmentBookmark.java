package com.example.resepku;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class FragmentBookmark extends Fragment {

    RecyclerView rvBookmarksReceipes;
    ArrayList<Meal> listMeal;
    MainActivity parent;
    ReceipesAdapter receipesAdapter;
    ProgressBar pgBarBookmarks;

    public FragmentBookmark() {
        // Required empty public constructor
    }

    public static FragmentBookmark newInstance(MainActivity tempParent) {
        FragmentBookmark fragment = new FragmentBookmark();
        fragment.parent = tempParent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMeal = new ArrayList<>();
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
        pgBarBookmarks = view.findViewById(R.id.pgBarBookmarks);
        rvBookmarksReceipes = view.findViewById(R.id.rvBookmarksReceipes);
        rvBookmarksReceipes.setLayoutManager(new GridLayoutManager(getContext(), 2));

        receipesAdapter = new ReceipesAdapter(getContext(), this, listMeal);
        rvBookmarksReceipes.setAdapter(receipesAdapter);

        pgBarBookmarks.setVisibility(View.VISIBLE);
        new TaskGetBookmarks().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 100) {
            pgBarBookmarks.setVisibility(View.VISIBLE);
            new TaskGetBookmarks().execute();
        }
    }

    private class TaskGetBookmarks extends AsyncTask<Void, Void, List<Bookmark>> {
        @Override
        protected List<Bookmark> doInBackground(Void... voids) {
            listMeal.clear();
            return parent.getDB().bookmarkDao().getAll(parent.getUserLogin().getId());
        }

        @Override
        protected void onPostExecute(List<Bookmark> bookmarks) {
            super.onPostExecute(bookmarks);
            if (bookmarks.size() > 0) {
                for (Bookmark bookmark : bookmarks) {
                    listMeal.add(bookmark.toMeal());
                }

                pgBarBookmarks.setVisibility(View.GONE);
                receipesAdapter.notifyDataSetChanged();
            }
            else {
                StringRequest getBookmarkRequest = new StringRequest(Request.Method.POST, getString(R.string.api_get_bookmark), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray responseArr = responseObj.getJSONArray("data");
                            for (int i=0; i < responseArr.length(); i++) {
                                JSONObject obj = responseArr.getJSONObject(i);
                                Bookmark bookmark = new Bookmark(obj.getInt("user_id"), obj.getInt("food_id"), obj.getString("name"), obj.getString("image"));
                                new TaskSaveBookmark(parent.getDB()).execute(bookmark);

                                Meal meal = bookmark.toMeal();
                                new TaskAddResep(parent.getDB()).execute(meal);
                                listMeal.add(meal);

                                pgBarBookmarks.setVisibility(View.GONE);
                                receipesAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgBarBookmarks.setVisibility(View.GONE);

                        if (error.networkResponse != null){
                            String res = new String(error.networkResponse.data);
                            try {
                                JSONObject resObj = new JSONObject(res);
                                Toast.makeText(getContext(), resObj.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                            body.put("token", parent.getUserLogin().getAccessToken());
                            body.put("user_id", parent.getUserLogin().getId());
                            return body.toString().getBytes(StandardCharsets.UTF_8);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(getBookmarkRequest);
            }
        }
    }

}