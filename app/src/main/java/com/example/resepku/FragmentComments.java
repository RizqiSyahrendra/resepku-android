package com.example.resepku;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Collections;


public class FragmentComments extends Fragment {
    ArrayList<Comment> listComment;
    RecyclerView rvComments;
    MaterialButton btnCommentSend;
    EditText txtCommentsInput;
    CommentsAdapter commentsAdapter;
    DetailActivity parent;
    ProgressBar pgBarComments;
    TextView txtNoCommentsFound;

    public FragmentComments() {
        // Required empty public constructor
    }

    public static FragmentComments newInstance(DetailActivity tempParent) {
        FragmentComments fragment = new FragmentComments();
        fragment.parent = tempParent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listComment = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        btnCommentSend = view.findViewById(R.id.btnCommentSend);
        txtCommentsInput = view.findViewById(R.id.txtCommentsInput);
        pgBarComments = view.findViewById(R.id.pgBarComments);
        txtNoCommentsFound = view.findViewById(R.id.txtNoCommentsFound);

        commentsAdapter = new CommentsAdapter(getContext(), this, listComment);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentsAdapter);
        loadComments();

        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    public UserLogin getUserLogin() {
        return parent.getUserLogin();
    }

    private void sendComment() {
        String commentText = txtCommentsInput.getText().toString();
        if (commentText.trim().equals("")) {
            Toast.makeText(getContext(), "Comment is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        pgBarComments.setVisibility(View.VISIBLE);
        txtNoCommentsFound.setVisibility(View.GONE);

        StringRequest addCommentRequest = new StringRequest(Request.Method.POST, getString(R.string.api_add_comments), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pgBarComments.setVisibility(View.GONE);

                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONObject dataComment = responseObj.getJSONObject("data");
                    Comment comment = new Comment(
                            dataComment.getInt("id"),
                            dataComment.getInt("user_id"),
                            dataComment.getString("name"),
                            dataComment.getString("comment"),
                            dataComment.getString("created_at"),
                            dataComment.getString("avatar")
                    );

                    listComment.add(0, comment);
                    Toast.makeText(getContext(), responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshComments();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pgBarComments.setVisibility(View.GONE);

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
                    body.put("food_id", parent.getActiveMeal().getId());
                    body.put("comment", commentText);
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
        requestQueue.add(addCommentRequest);
    }

    private void refreshComments() {
        txtCommentsInput.setText("");
        commentsAdapter.notifyDataSetChanged();
    }

    private void loadComments() {
        pgBarComments.setVisibility(View.VISIBLE);
        txtNoCommentsFound.setVisibility(View.GONE);

        StringRequest getCommentsRequest = new StringRequest(Request.Method.POST, getString(R.string.api_get_comments), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pgBarComments.setVisibility(View.GONE);
                txtNoCommentsFound.setVisibility(View.GONE);

                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray comments = responseObj.getJSONArray("data");
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject objComment = comments.getJSONObject(i);
                        Comment comment = new Comment(
                                objComment.getInt("id"),
                                objComment.getInt("user_id"),
                                objComment.getString("name"),
                                objComment.getString("comment"),
                                objComment.getString("created_at"),
                                objComment.getString("avatar")
                        );

                        listComment.add(comment);
                    }

                    commentsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pgBarComments.setVisibility(View.GONE);

                if (error.networkResponse != null){
                    if (error.networkResponse.statusCode == 404) {
                        txtNoCommentsFound.setVisibility(View.VISIBLE);
                    }
                    else {
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
                    body.put("food_id", parent.getActiveMeal().getId());
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
        requestQueue.add(getCommentsRequest);
    }

    public void deleteComment(int idxComment, int idComment) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Comment")
                .setMessage("Do you want to delete this comment?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest deleteCommentRequest = new StringRequest(Request.Method.POST, getString(R.string.api_delete_comments), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject responseObj = new JSONObject(response);
                                    listComment.remove(idxComment);

                                    refreshComments();
                                    Toast.makeText(getContext(), responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
                                    body.put("id", idComment);
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
                        requestQueue.add(deleteCommentRequest);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.show();
    }
}