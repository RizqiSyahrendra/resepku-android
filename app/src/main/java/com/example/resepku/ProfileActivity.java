package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {
    EditText txtProfileName, txtProfileEmail, txtProfilePassword, txtProfileConfirmPassword;
    Button btnProfileSave;
    AppDatabase db;
    UserLogin userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        txtProfilePassword = findViewById(R.id.txtProfilePassword);
        txtProfileConfirmPassword = findViewById(R.id.txtProfileConfirmPassword);
        btnProfileSave = findViewById(R.id.btnProfileSave);

        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();

        try {
            userLogin = new TaskGetUserLogin(db).execute().get();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        txtProfileName.setText(userLogin.getName());
        txtProfileEmail.setText(userLogin.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickUpdate(View view) {
        btnProfileSave.setEnabled(false);
        String name = txtProfileName.getText().toString();
        String email = txtProfileEmail.getText().toString();
        String password = txtProfilePassword.getText().toString();
        String confirmPassword = txtProfileConfirmPassword.getText().toString();

        if(!password.equals("") || !confirmPassword.equals("")){
            if(password.equals("")) {
                Toast.makeText(this, "Password can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(confirmPassword.equals("")) {
                Toast.makeText(this, "Confirm Password can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        new UpdateUserLoginTask().execute(userLogin);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.api_update_user), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btnProfileSave.setEnabled(true);

                try {
                    JSONObject responseObj = new JSONObject(response);
                    String resultMessage = responseObj.getString("message");
                    Toast.makeText(ProfileActivity.this, resultMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnProfileSave.setEnabled(true);
                if (error.networkResponse != null) {
                    String body = new String(error.networkResponse.data);
                    try {
                        JSONObject result = new JSONObject(body);
                        Toast.makeText(ProfileActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
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
                    body.put("id", userLogin.getId());
                    body.put("name", name);
                    body.put("password", password);
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
    }

    private class UpdateUserLoginTask extends AsyncTask<UserLogin, Void, Void> {
        @Override
        protected Void doInBackground(UserLogin... userLogins) {
            db.userLoginDao().update(txtProfileName.getText().toString(), userLogin.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
//            startActivity(mainIntent);
//            finish();
//            super.onPostExecute(aVoid);
        }
    }
}