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
        // Check if the admin user already exists


//        long result=db.addUser("Admin User", "admin", "admin", "987654321", true);
//
//        if (result > 0) {
//            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
//        }
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
        
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        User user = dbHandler.getUser(emailText, passwordText);
        
        if (user != null) {
            // Save login state and user info
            SharedPreferences loginPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = loginPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putInt("userId", user.getId());
            editor.putString("userEmail", emailText);
            editor.apply();
            
            // Start MainActivity
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