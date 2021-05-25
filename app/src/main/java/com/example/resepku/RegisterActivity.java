package com.example.resepku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        btnSignUpSubmit.setEnabled(false);
        String name = txtRegisterName.getText().toString().trim();
        String email = txtRegisterEmail.getText().toString().trim();
        String password = txtRegisterPassword.getText().toString().trim();
        String confirmPassword = txtRegisterConfirmPassword.getText().toString().trim();

        //validasi required
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equalsIgnoreCase("")) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassword.equalsIgnoreCase("")) {
            Toast.makeText(this, "Confirm Password is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        //cek konfirmasi password
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password is not match!", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void resetForm() {
        txtRegisterName.setText("");
        txtRegisterEmail.setText("");
        txtRegisterPassword.setText("");
        txtRegisterConfirmPassword.setText("");
    }

}