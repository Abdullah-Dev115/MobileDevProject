package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private EditText email, password;
    private Button loginBtn;
    private TextView signUpLink, forgotPassword;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        db = new DatabaseHandler(this);

        initViews();
        
        loginBtn.setOnClickListener(v -> loginUser());
        signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
            finish();
        });
    }
    
    private void initViews() {
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_login_btn);
        signUpLink = findViewById(R.id.login_signup);
        forgotPassword = findViewById(R.id.login_forgot_pswd);
    }
    
    private void loginUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.checkUser(emailText, passwordText)) {
            int userId = db.getUserId(emailText); // Retrieve userId based on the email

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Save login state, user email, and userId
            SharedPreferences loginPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = loginPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userEmail", emailText);
            editor.putInt("userId", userId);  // Save the userId
            editor.apply();

            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataToPrefs(String username, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.apply();
    }

}