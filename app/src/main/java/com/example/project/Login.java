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

        handleLogin(emailText, passwordText);
    }

    private void handleLogin(String email, String password) {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        User user = dbHandler.getUser(email, password);
        
        if (user != null) {
            // Save user data to SharedPreferences
            saveUserDataToPrefs(user.getUsername(), user.getEmail(), user.getPhone());
            
            // Debug log
            Log.d("Login", "User found - Username: " + user.getUsername() + 
                          ", Email: " + user.getEmail() + 
                          ", Phone: " + user.getPhone());
            
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