package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    private TextView usernameText;
    private TextView emailText;
    private TextView phoneText;
    private Button logoutButton;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHandler dbHandler;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        phoneText = findViewById(R.id.phone_text);
        logoutButton = findViewById(R.id.logout_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Initialize DatabaseHandler and SharedPreferences
        dbHandler = new DatabaseHandler(this);
        loginPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        
        // Load user data
        loadUserData();
        
        // Set up bottom navigation
        setupBottomNavigation();

        // Set up logout button click
        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        String userEmail = loginPreferences.getString("userEmail", null);
        if (userEmail != null) {
            User user = dbHandler.getUserByEmail(userEmail);
            if (user != null) {
                usernameText.setText(user.getUsername());
                emailText.setText(user.getEmail());
                phoneText.setText(user.getPhone());
            }
        }
    }

    private void logout() {
        // Clear login preferences
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.profile);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(this, EventsActivity.class));
                finish();
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(this, LostItemsActivity.class));
                finish();
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(this, FoundItemsActivity.class));
                finish();
            } else if (itemId == R.id.report_item) {
                startActivity(new Intent(this, ReportItemActivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                return true;
            }
            return true;
        });
    }
}