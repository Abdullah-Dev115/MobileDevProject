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
    private SharedPreferences sharedPreferences;

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

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        
        // Debug: Print all SharedPreferences values
        Map<String, ?> allPrefs = sharedPreferences.getAll();
        Log.d("UserProfileActivity", "All SharedPreferences values:");
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            Log.d("UserProfileActivity", entry.getKey() + ": " + entry.getValue().toString());
        }

        // Load user data
        loadUserData();
        
        // Set up bottom navigation
        setupBottomNavigation();

        // Set up logout button click
        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        // Get default values in case data is not found
        String defaultUsername = "No username set";
        String defaultEmail = "No email set";
        String defaultPhone = "No phone number set";

        // Get the data with defaults if not found
        String username = sharedPreferences.getString("username", defaultUsername);
        String email = sharedPreferences.getString("email", defaultEmail);
        String phone = sharedPreferences.getString("phone", defaultPhone);

        // Debug logging
        Log.d("UserProfileActivity", "Loading user data:");
        Log.d("UserProfileActivity", "Username: " + username);
        Log.d("UserProfileActivity", "Email: " + email);
        Log.d("UserProfileActivity", "Phone: " + phone);

        // Set the text with proper null checking
        usernameText.setText(username != null ? username : defaultUsername);
        emailText.setText(email != null ? email : defaultEmail);
        phoneText.setText(phone != null ? phone : defaultPhone);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
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