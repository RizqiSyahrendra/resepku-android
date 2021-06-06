package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText txtLoginEmail, txtLoginPassword;
    MaterialButton btnSignInSubmit;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        db = Room.databaseBuilder(this, AppDatabase.class, "db_resep").build();

        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        btnSignInSubmit = findViewById(R.id.btnSignInSubmit);
    }

    public void onClickLogin(View view) {
        btnSignInSubmit.setEnabled(false);
        String email = txtLoginEmail.getText().toString();
        String password = txtLoginPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.api_login), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btnSignInSubmit.setEnabled(true);

                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONObject responseData = responseObj.getJSONObject("data");
                    int resultId = responseData.getInt("id");
                    String resultName = responseData.getString("name");
                    String resultEmail = responseData.getString("email");
                    String resultAccessToken = responseData.getString("access_token");
                    UserLogin userLogin = new UserLogin(resultId, resultName, resultEmail, resultAccessToken);
                    new AddUserLoginTask().execute(userLogin);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Terjadi kesalahan login", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnSignInSubmit.setEnabled(true);
                String body = new String(error.networkResponse.data);
                try {
                    JSONObject result = new JSONObject(body);
                    Toast.makeText(LoginActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
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
                    body.put("email", email);
                    body.put("password", password);
                    body.put("token", getString(R.string.api_token));
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

    public void onClickRegister(View view) {
        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    private void resetForm() {
        txtLoginEmail.setText("");
        txtLoginPassword.setText("");
    }

    private class AddUserLoginTask extends AsyncTask<UserLogin, Void, Void> {
        @Override
        protected Void doInBackground(UserLogin... userLogins) {
            db.userLoginDao().insert(userLogins[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
            super.onPostExecute(aVoid);
        }
    }

}