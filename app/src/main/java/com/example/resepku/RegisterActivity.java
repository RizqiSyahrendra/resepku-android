package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText txtRegisterName, txtRegisterEmail, txtRegisterPassword, txtRegisterConfirmPassword;
    MaterialButton btnSignUpSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        txtRegisterName = findViewById(R.id.txtRegisterName);
        txtRegisterEmail = findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        txtRegisterConfirmPassword = findViewById(R.id.txtRegisterConfirmPassword);
        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
    }

    public void onClickLogin(View view) {
        finish();
    }

    public void onClickRegister(View view) {
        if (!Koneksi.isOnline(this)) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        btnSignUpSubmit.setEnabled(false);
        String name = txtRegisterName.getText().toString().trim();
        String email = txtRegisterEmail.getText().toString().trim();
        String password = txtRegisterPassword.getText().toString().trim();
        String confirmPassword = txtRegisterConfirmPassword.getText().toString().trim();

        //validasi required
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Name is empty!", Toast.LENGTH_SHORT).show();
            btnSignUpSubmit.setEnabled(true);
            return;
        }
        if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            btnSignUpSubmit.setEnabled(true);
            return;
        }
        if (password.equalsIgnoreCase("")) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            btnSignUpSubmit.setEnabled(true);
            return;
        }
        if (confirmPassword.equalsIgnoreCase("")) {
            Toast.makeText(this, "Confirm Password is empty!", Toast.LENGTH_SHORT).show();
            btnSignUpSubmit.setEnabled(true);
            return;
        }

        //cek konfirmasi password
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password is not match!", Toast.LENGTH_SHORT).show();
            btnSignUpSubmit.setEnabled(true);
            return;
        }

        StringRequest registerRequest = new StringRequest(Request.Method.POST, getString(R.string.api_register), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btnSignUpSubmit.setEnabled(true);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    Toast.makeText(RegisterActivity.this, responseObject.getString("message"), Toast.LENGTH_LONG).show();
                    resetForm();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnSignUpSubmit.setEnabled(true);

                String body = new String(error.networkResponse.data);
                try {
                    JSONObject result = new JSONObject(body);
                    Toast.makeText(RegisterActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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
                    body.put("name", name);
                    body.put("email", email);
                    body.put("password", password);
                    body.put("confirm_password", confirmPassword);
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
        requestQueue.add(registerRequest);
    }

    private void resetForm() {
        txtRegisterName.setText("");
        txtRegisterEmail.setText("");
        txtRegisterPassword.setText("");
        txtRegisterConfirmPassword.setText("");
    }

}